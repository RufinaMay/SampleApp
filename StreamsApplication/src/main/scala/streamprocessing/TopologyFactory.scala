package streamprocessing

import json4s.Json4sFhirExtensions.JValueFhirExtended
import json4s.Json4sGenericExtensions.JValueExtended
import metrics.MetricsReporter
import models.ResourceType
import models.Topics.{organizationTopic, resourceInputTopic, resourceOutputTopic}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.scala.kstream.Branched
import org.apache.kafka.streams.{StreamsBuilder, Topology}
import org.json4s.JValue
import serialization.JsonSerde

object TopologyFactory {

  def createTopology: Topology = {
    val builder: StreamsBuilder = new StreamsBuilder
    val jsonSerde: Serde[JValue] = new JsonSerde
    val kStream: KStream[String, JValue] = builder.stream(resourceInputTopic, Consumed.`with`(Serdes.String, jsonSerde))
    val organizationTable: GlobalKTable[String, JValue] = builder.globalTable[String, JValue](organizationTopic, Consumed.`with`(Serdes.String, jsonSerde))

    kStream
      .filter((_, v) => v.resourceType.contains(ResourceType.organization))
      .to(organizationTopic)

    kStream
      .split
      .branch(
        selectOrganization,
        Branched.withConsumer(
          ks => {
            ks.to(organizationTopic)(Produced.valueSerde(jsonSerde))
          }
        )
      )
      .branch(
        selectPatient,
        Branched.withFunction(
          _
            .selectKey((_, v) => v.organizationId.getOrElse(""))
            .leftJoin(organizationTable)((key, _) => key, joinPatientToOrganization)
            .selectKey((_, v) => v.id.getOrElse(""))
        )
      )
      .defaultBranch(
        Branched.withFunction(
          _.peek((_, data) =>
            MetricsReporter.reportMessageProcessed(data.resourceType.getOrElse("Unknown"))
          )
        )
      )
      .forEach {
        case (_, branch) =>
          branch
            .to(resourceOutputTopic, Produced.`with`(Serdes.String, jsonSerde))
      }

    builder.build()
  }

  private def selectOrganization: Predicate[String, JValue] = (_: String, value: JValue) =>
    value.resourceType.contains(ResourceType.organization)

  private def selectPatient: Predicate[String, JValue] = (_: String, value: JValue) =>
    value.resourceType.contains(ResourceType.patient)

  private def joinPatientToOrganization(patient: JValue, organizationJson: JValue): JValue = {
    // todo: handle situation when organization does not have name
    val organizationName = organizationJson \ "name"
    patient.upsertJsonProperty("managingOrganization.name", organizationName)
  }


}

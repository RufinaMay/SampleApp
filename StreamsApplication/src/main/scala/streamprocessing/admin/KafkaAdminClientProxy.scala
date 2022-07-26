package streamprocessing.admin

import com.typesafe.config.Config
import org.apache.kafka.clients.admin._

import java.util
import java.util.Properties
import scala.jdk.CollectionConverters.asJavaCollectionConverter

class KafkaAdminClientProxy(adminClient: AdminClient) extends KafkaAdminClient {
  override def listTopics(): ListTopicsResult = {
    adminClient.listTopics()
  }

  override def createTopics(newTopics: util.Collection[NewTopic]): CreateTopicsResult = {
    adminClient.createTopics(newTopics)
  }

  override def describeConsumerGroups(groupIds: Seq[String], options: DescribeConsumerGroupsOptions): DescribeConsumerGroupsResult = {
    adminClient.describeConsumerGroups(groupIds.asJavaCollection, options)
  }

  override def listConsumerGroupOffsets(groupId: String, options: ListConsumerGroupOffsetsOptions): ListConsumerGroupOffsetsResult = {
    adminClient.listConsumerGroupOffsets(groupId, options)
  }

  override def topicExists(topicName: String): Boolean = {
    listTopics().names.get.contains(topicName)
  }
}

object KafkaAdminClientProxy {
  def create(config: Config): KafkaAdminClientProxy = {
    val properties = adminClientProperties(config)
    val adminClient = AdminClient.create(properties)
    new KafkaAdminClientProxy(adminClient)
  }

  def adminClientProperties(config: Config): Properties = {
    val properties = new Properties()
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrapServer"))
    properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000")
    properties
  }
}

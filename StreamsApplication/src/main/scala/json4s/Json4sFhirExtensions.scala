package json4s

import json4s.JsonConversion.formats
import org.json4s.JValue

object Json4sFhirExtensions {

  implicit class JValueFhirExtended(value: JValue) {

    def id: Option[String] = {
      (value \ "id").extractOpt[String]
    }

    def resourceType: Option[String] = {
      (value \ "resourceType").extractOpt[String]
    }

    def patientId: Option[String] = {
      (value \ "patient" \ "reference").extractOpt[String]
    }

    def organizationId: Option[String] = {
      (value \ "managingOrganization" \ "reference").extractOpt[String].flatMap(_.split("/").lastOption)
    }

  }
}

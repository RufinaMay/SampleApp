package json4s

import org.json4s.{JField, JNothing, JObject, JValue}

object Json4sGenericExtensions {
  implicit class JValueExtended(value: JValue) {

    def upsertJsonProperty(path: String, newJValue: JValue): JValue = {
      val pathList = path.split("\\.").toList

      def apply(pathList: List[String], json: JValue): JValue = {
        pathList match {
          case Nil =>
            json

          case (currentProperty: String) :: (pathList: List[String]) =>
            val propertyExists = (json \ currentProperty) != JNothing
            val jsonValue = if (propertyExists) json else json merge JObject(currentProperty -> newJValue)

            jsonValue match {
              case JObject(fields) =>
                JObject(
                  fields.map {
                    case JField(`currentProperty`, childObject) =>
                      JField(currentProperty, if (pathList == Nil) newJValue else apply(pathList, childObject))

                    case field =>
                      field
                  }
                )

              case other =>
                other
            }
        }
      }

      apply(pathList, value)
    }

  }
}

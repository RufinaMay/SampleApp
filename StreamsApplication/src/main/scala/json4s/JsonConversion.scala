package json4s

import org.json4s.{DefaultFormats, Formats}

object JsonConversion {
  implicit val formats: Formats = DefaultFormats
}

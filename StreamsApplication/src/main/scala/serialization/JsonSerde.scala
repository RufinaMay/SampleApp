package serialization

import org.apache.kafka.common.serialization._
import org.json4s.JValue
import org.json4s.jackson.JsonMethods.{mapper, parse}

class JsonSerializer extends Serializer[JValue] {
  override def serialize(topic: String, data: JValue): Array[Byte] = {
    mapper.writeValueAsBytes(data)
  }
}

class JsonDeserializer extends Deserializer[JValue] {
  private val stringDeserializer = new StringDeserializer

  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = {
    stringDeserializer.configure(configs, isKey)
  }

  override def deserialize(topic: String, data: Array[Byte]): JValue = {
    parse(
      stringDeserializer.deserialize(topic, data)
    )
  }
}

class JsonSerde extends Serde[JValue] {
  override def deserializer(): Deserializer[JValue] = new JsonDeserializer

  override def serializer(): Serializer[JValue] = new JsonSerializer
}
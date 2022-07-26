package streamprocessing

import com.typesafe.config.Config
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{Serdes, StringSerializer}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import java.util.Properties

object StreamFactory {

  def createStream(config: Config): KafkaStreams = {
    val topology = TopologyFactory.createTopology
    val properties = streamProperties(config)
    new KafkaStreams(topology, properties)
  }

  def streamProperties(config: Config): Properties = {
    val properties = new Properties()
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrapServer"))
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, config.getString("application.name"))
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    properties.put(ProducerConfig.ACKS_CONFIG, "all")
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, config.getString("kafka.streams.replicationFactor"))
    properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, config.getString("kafka.streams.numberStreamThreads"))
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getString("kafka.streams.bufferSizeBytes"))
    properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, config.getString("kafka.streams.maxBlockMs"))
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getString("kafka.streams.batchSize"))
    properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip")
    properties
  }
}

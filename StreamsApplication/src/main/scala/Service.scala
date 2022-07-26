import com.typesafe.config.{Config, ConfigFactory}
import httpserver.AkkaHttpRouting
import streamprocessing.StreamProcessor

object Service extends App {

    val config: Config = ConfigFactory.load()

    val host = config.getString("application.enricher-host")
    val port = config.getInt("application.enricher-port")

    AkkaHttpRouting.start(host, port)

    StreamProcessor.start(config)

}

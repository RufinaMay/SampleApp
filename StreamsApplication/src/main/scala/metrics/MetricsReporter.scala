package metrics

import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.{Counter, Gauge}

object MetricsReporter {

  val metricPrefix: String = "rufApp"

  private val processedMessagesCounter: Counter = createCounter(
    metricName = "message_processed_total",
    help = "Amount of messages processed",
    labels = List("resourceType")
  )

  private def createFullMetricName(metricName: String) = s"${metricPrefix}_$metricName"

  def createGauge(metricName: String, help: String, labels: Seq[String] = Nil): Gauge = {
    Gauge.build(createFullMetricName(metricName), help).labelNames(labels: _*).register()
  }

  def createCounter(metricName: String, help: String, labels: Seq[String] = Nil): Counter = {
    Counter.build(createFullMetricName(metricName), help).labelNames(labels: _*).register()
  }

  def reportMessageProcessed(resourceType: String): Unit = {
    processedMessagesCounter.labels(resourceType).inc()
  }

  def startHttpServer(prometheusPort: Int): Unit = {
    new HTTPServer(prometheusPort)
  }

}
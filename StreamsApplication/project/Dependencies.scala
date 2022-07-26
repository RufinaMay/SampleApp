import sbt._

object Dependencies {

  lazy val allDependencies: Seq[ModuleID] =
    kafkaDependencies ++
      typesafeDependencies ++
      logbackDependencies ++
      testDependencies ++
      json4sDependencies ++
      scaldiDependencies ++
      akkaDependencies ++
      prometheusClientDependencies


  private lazy val kafkaVersion = "3.1.0"
  private lazy val typesafeConfigVersion = "1.4.2"
  private lazy val logbackVersion = "1.2.11"
  private lazy val scalatestVersion = "3.2.11"
  private lazy val json4sVersion = "4.0.5"
  private lazy val scaldiVersion = "0.6.2"
  private lazy val scalamockVersion = "5.2.0"
  private lazy val scalamockitoVersion = "1.17.5"
  private lazy val akkaHttpVersion = "10.2.9"
  private lazy val akkaVersion = "2.6.18"
  private lazy val prometheusClientVersion = "0.15.0"

  val kafkaDependencies: Seq[ModuleID] = Seq(
    "org.apache.kafka" % "kafka-streams" % kafkaVersion,
    "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion,
    "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVersion % "test"
  )

  val typesafeDependencies: Seq[ModuleID] = Seq(
    "com.typesafe" % "config" % typesafeConfigVersion
  )

  val logbackDependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  val testDependencies: Seq[ModuleID] = Seq(
    "org.scalamock" %% "scalamock" % scalamockVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.mockito" %% "mockito-scala" % scalamockitoVersion % "test"
  )

  val json4sDependencies: Seq[ModuleID] = Seq(
    "org.json4s" %% "json4s-jackson" % json4sVersion
  )

  val scaldiDependencies: Seq[ModuleID] = Seq(
    "org.scaldi" %% "scaldi" % scaldiVersion
  )

  val akkaDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  )

  val prometheusClientDependencies: Seq[ModuleID] = Seq(
    "io.prometheus" % "simpleclient" % prometheusClientVersion,
    "io.prometheus" % "simpleclient_httpserver" % prometheusClientVersion
  )

}

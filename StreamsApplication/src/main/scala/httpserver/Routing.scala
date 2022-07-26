package httpserver

trait Routing {
  def start(host: String, port: Int): Unit
}

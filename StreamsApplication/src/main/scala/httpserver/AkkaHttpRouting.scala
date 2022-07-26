package httpserver

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import org.json4s.jackson.Serialization
import org.json4s.{JArray, JString}
import scaldi.{Injectable, Injector}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object AkkaHttpRouting extends Routing {
  private implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Akka-Http")
  private implicit val executionContext: ExecutionContextExecutor = system.executionContext

  override def start(host: String, port: Int): Unit = {
    Http().newServerAt(host, port).bind(route)
      .onComplete {
        case Success(_) =>
        case Failure(e) =>
          e.printStackTrace()
          system.terminate()
      }
  }

  private val route = {
    get {
      path(Segment) {
        case "ping" => complete("pong")
      }
    }
  }

}
package streamprocessing

import com.typesafe.config.Config

import java.util.concurrent.CountDownLatch

object StreamProcessor {

  def start(config: Config): Unit = {
    val latch = new CountDownLatch(1)

    try {
      val streams = StreamFactory.createStream(config)
      streams.start()

      Runtime.getRuntime.addShutdownHook(new Thread() {
        override def run(): Unit = {
          streams.close()
        }
      })

      latch.await()
    }
    catch {
      case e: Throwable =>
        Runtime.getRuntime.halt(1)
    }

  }

}

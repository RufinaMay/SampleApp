import com.typesafe.sbt.SbtNativePackager.Docker
import com.typesafe.sbt.packager.Keys.{defaultLinuxInstallLocation, dockerBaseImage, dockerBuildOptions, dockerCommands, dockerLabels, dockerRepository, dockerUpdateLatest, dockerUsername}
import com.typesafe.sbt.packager.docker.ExecCmd
import sbt.Keys.{isSnapshot, name, updateOptions, version}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DockerImage {

  lazy val publishToRegistry = Seq(
    dockerBaseImage := "openjdk:latest",
    Docker / defaultLinuxInstallLocation := "/opt/" + name.value,
    dockerUpdateLatest := true,
    updateOptions := updateOptions.value.withLatestSnapshots(true),
    dockerRepository := Some("registry.something.net"),
    dockerUsername := Some("ruf"),
    Docker / version := {
      if (isSnapshot.value) s"${version.value}-$timestamp" else version.value
    },
    dockerLabels := Map(
      "maintainer" -> "Team Ruf",
      "description" -> "Ruf test app"
    ),
    dockerBuildOptions += "--no-cache",
    Docker / dockerCommands := dockerCommands.value.filterNot {
      case ExecCmd("RUN", args@_*) => args.contains("chown")
      case cmd => false
    }
  )

  private def timestamp: String =
    DateTimeFormatter.ofPattern("yyyyMMdd'.'HHmmss").format(LocalDateTime.now())
}
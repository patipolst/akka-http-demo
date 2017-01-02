name := "akka-http-demo"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "10.0.1"
  val slickVersion = "3.2.0-M2"
  val circeVersion = "0.6.1"
  Seq(
    "com.typesafe.akka"   %% "akka-http"            % akkaVersion,
    "com.typesafe.akka"   %% "akka-http-testkit"    % akkaVersion,
    "de.heikoseeberger"   %% "akka-http-circe"      % "1.11.0",
    "io.circe"            %% "circe-core"           % circeVersion,
    "io.circe"            %% "circe-generic"        % circeVersion,
    "io.circe"            %% "circe-parser"         % circeVersion,
    "com.pauldijou"       %% "jwt-circe"            % "0.9.2",
    "com.wix"             %% "accord-core"          % "0.6.1",
    "com.typesafe.slick"  %% "slick"                % slickVersion,
    "com.h2database"      %  "h2"                   % "1.4.193",
    "org.slf4j"           %  "slf4j-nop"            % "1.7.22",
    "org.scalatest"       %% "scalatest"            % "3.0.1"           % "test"
  )
}

coverageExcludedPackages := "am.Main;am.api.routes.PingRoute;am.utils.*"

parallelExecution in Test := false

enablePlugins(JavaAppPackaging)

dockerExposedPorts := Seq(9000)

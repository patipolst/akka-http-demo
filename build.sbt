name := "akka-http-demo"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "10.0.5"
  val slickVersion = "3.2.0"
  val circeVersion = "0.7.0"
  Seq(
    "com.typesafe.akka"   %% "akka-stream"          % "2.4.17",
    "com.typesafe.akka"   %% "akka-http"            % akkaVersion,
    "com.typesafe.akka"   %% "akka-http-testkit"    % akkaVersion,
    "de.heikoseeberger"   %% "akka-http-circe"      % "1.14.0",
    "io.circe"            %% "circe-core"           % circeVersion,
    "io.circe"            %% "circe-generic"        % circeVersion,
    "io.circe"            %% "circe-parser"         % circeVersion,
    "com.pauldijou"       %% "jwt-circe"            % "0.12.1",
    "com.wix"             %% "accord-core"          % "0.6.1",
    "com.typesafe.slick"  %% "slick"                % slickVersion,
    "com.h2database"      %  "h2"                   % "1.4.194",
    "org.slf4j"           %  "slf4j-nop"            % "1.7.25",
    "org.scalatest"       %% "scalatest"            % "3.0.1"           % "test"
  )
}

coverageExcludedPackages := "am.Main;am.api.routes.PingRoute;am.api.routes.TokenRoute;am.utils.*"

parallelExecution in Test := false

enablePlugins(JavaAppPackaging)

dockerExposedPorts := Seq(9000)

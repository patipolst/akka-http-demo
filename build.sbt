name := "akka-http-demo"
version := "1.0"
scalaVersion := "2.12.1"

libraryDependencies ++= {
  val akkaVersion = "10.0.1"
  val slickVersion = "3.2.0-M2"
  val circeVersion = "0.6.1"
  Seq(
    "org.scala-lang"      % "scala-compiler"        % scalaVersion.value  % "provided",
    "com.typesafe.akka"   %% "akka-http"            % akkaVersion,
    "com.typesafe.akka"   %% "akka-http-testkit"    % akkaVersion,
    "de.heikoseeberger"   %% "akka-http-circe"      % "1.11.0",
    "io.circe"            %% "circe-core"           % circeVersion,
    "io.circe"            %% "circe-generic"        % circeVersion,
    "io.circe"            %% "circe-parser"         % circeVersion,
    "com.wix"             %% "accord-core"          % "0.6.1",
    // "org.slf4j"           %  "slf4j-nop"            % "1.7.22",
    "ch.qos.logback"      % "logback-classic"       % "1.1.8",
    "com.typesafe.slick"  %% "slick"                % slickVersion,
    "com.h2database"      %  "h2"                   % "1.4.193",
    "org.scalatest"       %% "scalatest"            % "3.0.1"             % "test"
  )
}

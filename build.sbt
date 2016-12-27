name := "akka-http-demo"
version := "1.0"
scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka"   %% "akka-http"            % "10.0.1",
  "com.typesafe.akka"   %% "akka-http-testkit"    % "10.0.1",
  "de.heikoseeberger"   %% "akka-http-circe"      % "1.11.0",
  "io.circe"            %% "circe-core"           % "0.6.1",
  "io.circe"            %% "circe-generic"        % "0.6.1",
  "io.circe"            %% "circe-parser"         % "0.6.1",
  "com.typesafe.slick"  %% "slick"                % "3.2.0-M2",
  "com.h2database"      %  "h2"                   % "1.4.193",
  "org.scalatest"       %% "scalatest"            % "3.0.1"     % "test"
)

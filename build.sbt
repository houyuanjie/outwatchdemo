organization := "icu.harx"
name         := "outwatchdemo"
version      := "0.1.0"

scalaVersion := "2.13.8"
scalacOptions += "-Xsource:3"

enablePlugins(ScalaJSPlugin)
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies ++= Seq(
  "io.github.outwatch" %%% "outwatch"      % "1.0.0-RC5",
  "io.github.outwatch" %%% "outwatch-util" % "1.0.0-RC5",
  "org.scalatest"      %%% "scalatest"     % "3.2.10" % Test
)

enablePlugins(ScalaJSBundlerPlugin)
// makes scalajs-bundler use yarn instead of npm
useYarn                         := false
Test / requireJsDomEnv          := true
scalaJSUseMainModuleInitializer := true
// configure Scala.js to emit a JavaScript module instead of a top-level script
scalaJSLinkerConfig ~= (_.withModuleKind(
  ModuleKind.CommonJSModule
))

// hot reloading configuration:
// https://github.com/scalacenter/scalajs-bundler/issues/180
addCommandAlias("devwatch", "~; fastOptJS; copyFastOptJS")
addCommandAlias(
  "dev",
  "; compile; fastOptJS::startWebpackDevServer; devwatch; fastOptJS::stopWebpackDevServer"
)

webpack / version               := "4.46.0"
startWebpackDevServer / version := "3.11.2"
webpackDevServerExtraArgs       := Seq("--color")
webpackDevServerPort            := 8080

fastOptJS / webpackConfigFile   := Some(
  baseDirectory.value / "webpack.config.dev.js"
)
// https://scalacenter.github.io/scalajs-bundler/cookbook.html#performance
fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly()

// when running the "dev" alias, after every fastOptJS compile all artifacts are copied into
// a folder which is served and watched by the webpack devserver.
// this is a workaround for: https://github.com/scalacenter/scalajs-bundler/issues/180
lazy val copyFastOptJS: TaskKey[Unit] =
  TaskKey[Unit]("copyFastOptJS", "Copy javascript files to target directory")

copyFastOptJS := {
  val inDir  = (Compile / fastOptJS / crossTarget).value
  val outDir = (Compile / fastOptJS / crossTarget).value / "dev"
  val files  = Seq(
    name.value.toLowerCase + "-fastopt-loader.js",
    name.value.toLowerCase + "-fastopt.js"
  ).map(p => (inDir / p, outDir / p))

  IO.copy(
    files,
    overwrite = true,
    preserveLastModified = true,
    preserveExecutable = true
  )
}

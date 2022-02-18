/* ########## 信息 ########## */

organization := "icu.harx"
name         := "outwatchdemo"
version      := "0.1.0"

scalaVersion := "2.13.8"
scalacOptions += "-Xsource:3"

/* ########## 插件 ########## */

enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

/* ########## 依赖 ########## */

resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies ++= Seq(
  "io.github.outwatch" %%% "outwatch"      % "1.0.0-RC5",
  "io.github.outwatch" %%% "outwatch-util" % "1.0.0-RC5",
  "org.scalatest"      %%% "scalatest"     % "3.2.10" % Test
)

/* ########## 命令 ########## */

// hot reloading configuration:
// https://github.com/scalacenter/scalajs-bundler/issues/180
addCommandAlias(
  "devwatch",
  "~; fastOptJS; copyFastOptJS"
)

addCommandAlias(
  "dev",
  "; compile; fastOptJS::startWebpackDevServer; devwatch; fastOptJS::stopWebpackDevServer"
)

/* ########## 配置 ########## */

scalaJSUseMainModuleInitializer := true
scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))

useYarn                := false
Test / requireJsDomEnv := true

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
  ).map(jsFile => (inDir / jsFile, outDir / jsFile))

  IO.copy(
    files,
    overwrite = true,
    preserveLastModified = true,
    preserveExecutable = true
  )
}

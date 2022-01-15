package outwatchdemo.readme

import cats.effect.*
import colibri.*
import outwatch.*
import outwatch.dsl.*

import scala.concurrent.*
import scala.concurrent.duration.*
import scala.language.postfixOps

object DynamicContent {
  val reactiveProgramming: HtmlVNode =
    div(
      "Observable",
      Observable.interval(1 second)
    )

  val reactiveAttributes: HtmlVNode = {
    val boxWidth =
      Observable
        .interval(1 second)
        .map(n => if (n % 2 == 0) "100px" else "50px")

    div(
      width <-- boxWidth,
      height          := "50px",
      backgroundColor := "cornflowerblue",
      transition      := "width 0.5s"
    )
  }

  val reactiveModifiers: HtmlVNode =
    div(
      width  := "50px",
      height := "50px",
      Observable
        .interval(1.second)
        .map { i =>
          val color = if (i % 2 == 0) "tomato" else "cornflowerblue"

          backgroundColor := color
        }
    )

  val reactiveVNodes: HtmlVNode = {
    val nodes =
      Observable(div("I am delayed!"))
        .delay(5 second)

    div("Hello ", nodes)
  }

  implicit val ec: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.queue

  val renderingFutures: HtmlVNode =
    div(
      Future(1 + 1)
    )

  val higherOrderReactiveness: HtmlVNode =
    div(
      div(Observable.interval(1 second).map(n => Future(n * n))),
      div(Observable.interval(1 second).map(n => IO(2 * n))),
      div(IO { Observable.interval(1 second) })
    )

  implicit val cancel: CanCancel[Cancelable] =
    colibri.Cancelable.cancelCancelable

  val domLifecycleMangement: HtmlVNode =
    div(
      managed {
        SyncIO {
          ???
        }
      }
    )
}

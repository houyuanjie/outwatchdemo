package outwatchdemo.readme

import colibri.*
import org.scalajs.dom.{KeyCode, KeyboardEvent, console}
import outwatch.*
import outwatch.EmitterBuilder.Sync
import outwatch.dsl.*

object HandingEvents {
  val consoleLog: HtmlVNode =
    div(
      button(
        "Log a Click-Event",
        onClick.foreach { e => console.log(s"Event: $e") }
      ),
      " (Check the browser console to see the event)"
    )

  val mouse: HtmlVNode = {
    val x = Subject.behavior(0.0)

    div(
      div(
        "Hover to see mouse x-coordinate",
        onMouseMove.map(e => e.clientX) --> x,
        backgroundColor := "lightpink",
        cursor.crosshair
      ),
      div(" x = ", x)
    )
  }

  val counter: HtmlVNode = {
    val counter = Subject.behavior(0)

    div(
      button("-1", onClick(counter.map(_ - 1)) --> counter),
      button("+1", onClick(counter.map(_ + 1)) --> counter),
      button("reset", onClick.use(0) --> counter),
      div("counter: ", counter)
    )
  }

  val onEnter: Sync[KeyboardEvent, VDomModifier] =
    onKeyDown
      .filter(e => e.keyCode == KeyCode.Enter)
      .preventDefault

  val inputField: HtmlVNode = {
    val text      = Subject.behavior("")
    val submitted = Subject.behavior("")

    div(
      input(
        tpe := "text",
        value <-- text,
        onInput.value --> text,
        onEnter(text) --> submitted
      ),
      button("clear", onClick.use("") --> text),
      div("text: ", text),
      div("length: ", text.map(_.length)),
      div("submitted: ", submitted)
    )
  }

  val debouncedInput: HtmlVNode = {
    val text      = Subject.behavior("")
    val debounced = Subject.behavior("")

    div(
      input(
        tpe := "text",
        onInput.value --> text,
        onInput.value.debounceMillis(500) --> debounced
      ),
      div("text: ", text),
      div("debounced: ", debounced)
    )
  }

  val globalEvent: HtmlVNode = {
    val onKeyDown = events.document.onKeyDown.map(_.key)
    val onBlur    = events.window.onBlur.map(_ => "blur")
    val onFocus   = events.window.onFocus.map(_ => "focus")

    div(
      div("document onkeydown: ", onKeyDown),
      div("window focus: ", Observable.merge(onBlur, onFocus))
    )
  }
}

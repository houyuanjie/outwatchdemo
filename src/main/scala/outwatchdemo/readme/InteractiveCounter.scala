package outwatchdemo.readme

import outwatch.HtmlVNode
import outwatch.dsl.{button, div, onClick}

object InteractiveCounter {
  val component: HtmlVNode = {
    val counter = colibri.Subject.behavior(0)

    div(
      button("+1", onClick(counter.map(num => num + 1)) --> counter),
      counter
    )
  }
}

package outwatchdemo

import cats.effect.IO
import org.scalajs.dom.*
import outwatch.*
import outwatch.dsl.*

class OutwatchdemoSpec extends JSDomSpec {

  "You" should "probably add some tests" in {

    val message = "Hello World!"
    OutWatch.renderInto[IO]("#app", h1(message)).unsafeRunSync()

    document.body.innerHTML.contains(message) shouldBe true
  }
}

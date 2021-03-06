package net.yoshinorin.orchard.services.github.event.json

import io.circe.Json
import io.circe.parser.parse
import net.yoshinorin.orchard.definitions.action.ActionType
import net.yoshinorin.orchard.models.db.{Events, Repositories}
import net.yoshinorin.orchard.utils.File
import org.scalatest.FunSuite

// testOnly *EventSpec
class EventSpec extends FunSuite {

  val repositoryJson = File.readAll(System.getProperty("user.dir") + "/src/test/resources/data/json/repository.json")
  val repositoryInstance = net.yoshinorin.orchard.services.github.event.json.Repository(parse(repositoryJson).getOrElse(Json.Null))

  val json = File.readAll(System.getProperty("user.dir") + "/src/test/resources/data/json/issue.json")
  val instance = net.yoshinorin.orchard.services.github.event.json.Event(repositoryInstance.repository.get, parse(json).getOrElse(Json.Null))

  test("getConvertedCaseClass should return Events case class") {
    val eventsCaseClass = Some(
      Events(
        0,
        1234567890.toLong,
        "IssuesEvent",
        "YoshinoriN",
        repositoryInstance.repository.get.id,
        ActionType.Created.value,
        repositoryInstance.repository.get.name,
        1543926608
      )
    )
    assert(instance.getConvertedCaseClass == eventsCaseClass)
  }

  test("getConvertedCaseClass method should return None") {
    val json = """Not a JSON"""
    assert(Event(repositoryInstance.repository.get, parse(json).getOrElse(Json.Null)).getConvertedCaseClass.isEmpty)
  }

}

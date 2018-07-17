import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Todo(id: Int,
  title: String,
  completed: Boolean)

object Requests {

  val retrieveTodos: ChainBuilder = exec(
    http("retrieve-todos")
      .get("/")
      .check(status.is(200))
  )

  val createTodo: ChainBuilder = exec(
    http("create-todo")
      .post("/")
      .body(StringBody(
        s"""
           | {
           |   "title": "howdy todo ${UUID.randomUUID().toString}"
           | }
        """.stripMargin))
      .check(status.is(200))
      .check(jsonPath("$.id").find.saveAs("todo_id"))
  )

  val retrieveTodo: ChainBuilder = exec(
    http("retrieve-todo")
      .get("/${todo_id}")
      .check(status.is(200))
  )

  val updateTodo: ChainBuilder = exec(
    http("update-todo")
      .patch("/${todo_id}")
      .body(StringBody("{\"id\": \"${todo_id}\", \"title\": \"UPDATED howdy todo ${todo_id}\", \"completed\": true }"))
      .check(status.is(200))
  )

  val retrieveUpdatedTodo: ChainBuilder = exec(
    http("retrieve-updated-todo")
      .get("/${todo_id}")
      .check(status.is(200))
      .check(jsonPath("$.id").is("${todo_id}"))
      .check(jsonPath("$.completed").is("true"))
  )

  val deleteTodo: ChainBuilder = exec(
    http("delete-todo")
      .delete("/${todo_id}")
      .check(status.is(200))
  )

  val retrieveTodo404: ChainBuilder = exec(
    http("retrieve-todo-404")
      .get("/${todo_id}")
      .check(status.not(200))
  )
}

class TodosSimulation extends Simulation {

  val endpoint: String = System.getProperty("TODOS_API_ENDPOINT", "http://localhost:8009")
  val numberOfUsers: Int = System.getProperty("TODOS_API_USERS", "1").toInt
  val repeatTimes: Int = System.getProperty("TODOS_API_REPEAT", "1").toInt
  val pause: FiniteDuration = System.getProperty("TODOS_API_PAUSE", "1").toInt.millisecond
  val maxDuration: FiniteDuration = 1.minute
  val httpConf: Protocol = http
    .baseURL(endpoint)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  setUp(
    scenario("todos-crud-scenario")
      .exec(Requests.createTodo)
      .pause(pause)
      .exec(Requests.retrieveTodo)
      .pause(pause)
      .exec(Requests.updateTodo)
      .pause(pause)
      .exec(Requests.retrieveUpdatedTodo)
      .pause(pause)
      .exec(Requests.deleteTodo)
      .pause(pause)
      .exec(Requests.retrieveTodo404)
      .pause(pause)
      .inject(rampUsers(numberOfUsers).over(30 seconds))
  ).protocols(httpConf)
    .maxDuration(maxDuration)
}
package controllers

import javax.inject.Inject

import models.{Assignee}
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by shri on 14/12/15.
 */

class Assignees @Inject() (
                          val reactiveMongoApi: ReactiveMongoApi,
                          val messagesApi: MessagesApi
                          )
  extends Controller with MongoController with ReactiveMongoComponents {
  val collection = db[BSONCollection]("assignees")

  /** list all tickets */
  def index = Action.async { implicit request =>
    //    Async {
    val cursor = collection.find(
        BSONDocument(), BSONDocument()).cursor[Assignee]() // get all the fields of all the tickets
    val futureList = cursor.collect[List]() // convert it to a list of ticket
    futureList.map { assignees =>
      implicit val msg = messagesApi.preferred(request)
      Ok(Json.toJson(assignees))
    } // convert it to a JSON and return it
  }
}

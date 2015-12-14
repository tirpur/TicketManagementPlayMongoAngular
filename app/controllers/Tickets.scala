package controllers

import javax.inject.Inject

import models._
import play.api.http.MediaType.parse
import play.api.i18n.MessagesApi
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents, MongoController}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


/**
 * Created by shri on 12/12/15.
 */
class Tickets @Inject() (
                              val reactiveMongoApi: ReactiveMongoApi,
                              val messagesApi: MessagesApi
                              )
                            extends Controller with MongoController with ReactiveMongoComponents {
  val collection = db[BSONCollection]("tickets")

  /** list all tickets */
  def index = Action.async { implicit request =>
//    Async
     {
      val cursor = collection.find(
        BSONDocument(), BSONDocument()).cursor[Ticket]() // get all the fields of all the tickets
      val futureList = cursor.collect[List]() // convert it to a list of ticket
      futureList.map { tickets =>
        implicit val msg = messagesApi.preferred(request)
        Ok(Json.toJson(tickets))
      } // convert it to a JSON and return it
    }
  }

  
  
  /** create a ticket from the given JSON */
  def create() = Action.async(parse.json) { request =>
//    Async
    {
      val custname = request.body.\("custname").as[String]
      val email = request.body.\("email").as[String]
      val phone = request.body.\("phone").as[String]
      val comment = request.body.\("comment").as[String]
      val status = request.body.\("status").as[TicketStatus]
      val category = request.body.\("category").as[Category]
      val createdby = request.body.\("createdby").as[CSR]
      val assignedto = request.body.\("assignedto").asOpt[Assignee] match {
        case None => Some(Assignee("None","None"))
        case at:Option[Assignee] => at
      }

//      val status = if(assignedto.get.name.equals("None")) "New" else "Open"


/*      assignedTo match {
        case Non e => println("NONONONONONONONONONO")
        case _ => println("HEYEYEYE ITS NOT NONE")
      }*/
//      val assignedto = new Assignee("asd","asfd")
//      request.body.
//      if(assignedto == null) {
//        println("NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLss")
//      }

      /*
              doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Long]("timestamp").get,
        doc.getAs[String]("custname").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("phone").get,
        doc.getAs[CSR]("createdby").get,
        doc.getAs[String]("comment").get,
        doc.getAs[Assignee]("assignedto").get,
        doc.getAs[String]("status").get,
        doc.getAs[String]("category").get)
       */

      val ticket = Ticket(Option(BSONObjectID.generate),System.currentTimeMillis(),
        custname,email,phone,createdby,comment,assignedto.get,status,category) // create the ticket

      collection.insert(ticket).map(
        _ => {
          implicit val msg = messagesApi.preferred(request)
          Ok(Json.toJson(ticket)) // return the created ticket in a JSON
        })
    }
  }

  /** retrieve the ticket for the given id as JSON */
  def show(id: String) = Action.async(parse.empty) { request =>
//    Async
    {
      val objectID = BSONObjectID(id) // get the corresponding BSONObjectID
      // get the ticket having this id (there will be 0 or 1 result)
      val futureticket = collection.find(BSONDocument("_id" -> objectID)).one[Ticket]
      futureticket.map { ticket =>
        implicit val msg = messagesApi.preferred(request)
        Ok(Json.toJson(ticket))
      }
    }
  }

  /** update the ticket for the given id from the JSON body */
  def update(id: String) = Action.async(parse.json) { request =>
//    Async
    {
      val custname = request.body.\("custname").as[String]
      val email = request.body.\("email").as[String]
      val phone = request.body.\("phone").as[String]
      val comment = request.body.\("comment").as[String]
      val status = request.body.\("status").as[TicketStatus]
      val category = request.body.\("category").as[Category]
      val createdby = request.body.\("createdby").as[CSR]
//      val assignedto = request.body.\("assignedto").as[Assignee]
      val assignedto = request.body.\("assignedto").asOpt[Assignee] match {
        case None => Some(Assignee("None","None"))
        case at:Option[Assignee] => at
      }

//      val status = if(assignedto.get.name.equals("None")) "New" else "Open"

      val objectID = BSONObjectID(id) // get the corresponding BSONObjectID
      val modifier = BSONDocument( // create the modifier ticket
        "$set" -> BSONDocument(
          "custname" -> custname,
          "email" -> email,
          "phone" -> phone,
          "comment" -> comment,
          "status" -> status,
          "category"-> category,
          "createdby"-> createdby,
          "assignedto"->assignedto))
      collection.update(BSONDocument("_id" -> objectID), modifier).map(
        _ => {
          implicit val msg = messagesApi.preferred(request)
          Ok(Json.toJson(Ticket(Option(objectID),System.currentTimeMillis(),custname,email,
            phone,createdby,comment,assignedto.get,status,category)))
        }) // return the modified ticket in a JSON
    }
  }

  /** delete a ticket for the given id */
  def delete(id: String) = Action.async(parse.empty) { request =>
//    Async
    {
      val objectID = BSONObjectID(id) // get the corresponding BSONObjectID
      collection.remove(BSONDocument("_id" -> objectID)).map( // remove the ticket
        _ => {
          implicit val msg = messagesApi.preferred(request)
          Ok(Json.obj())}).recover { case _ => InternalServerError } // and return an empty JSON while recovering from errors if any
    }
  }
}

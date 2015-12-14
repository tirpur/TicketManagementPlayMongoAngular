package models

import play.api.libs.json.Json
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

/*
 * Author: Shri
 */

/*
	    timestamp: 1449928840756,
		name: 'MediaMelon',
		email: 'shri@mediamelon.com',
		phone: '9880048030',
		createdby: 'Shri|shri@redmart.com',
		comment: 'Offerred 50% discount but its not reflected in billing.',
        assignedto: 'vinay@redmart.com',
        status: 'Open',
        category: 'OfferDispute'
 */

case class Ticket(id: Option[BSONObjectID],
                  timestamp: Long,
                  custname: String,
                  email: String,
                  phone: String,
                  createdby: CSR,
                  comment: String,
                  assignedto: Assignee,
                  status: TicketStatus, //Make status table in mongo
                  category: Category //Make category table in mongo
                   )

object Ticket {
  /** serialize/Deserialize a Ticket into/from JSON value */
  implicit val TicketFormat = Json.format[Ticket]

  /** serialize a Ticket into a BSON */
  implicit object TicketBSONWriter extends BSONDocumentWriter[Ticket] {
    def write(ticket: Ticket): BSONDocument =
      BSONDocument(
        "_id" -> ticket.id.getOrElse(BSONObjectID.generate),
        "timestamp" -> System.currentTimeMillis(),
        "custname" -> ticket.custname,
        "email" -> ticket.email,
        "phone" -> ticket.phone,
        "createdby" -> ticket.createdby,
        "comment" -> ticket.comment,
        "assignedto" -> ticket.assignedto,
        "status" -> ticket.status,
        "category" -> ticket.category
      )
  }

  /** deserialize a Ticket from a BSON */
  implicit object TicketBSONReader extends BSONDocumentReader[Ticket] {
    def read(doc: BSONDocument): Ticket =
      Ticket(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Long]("timestamp").get,
        doc.getAs[String]("custname").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("phone").get,
        doc.getAs[CSR]("createdby").get,
        doc.getAs[String]("comment").get,
        doc.getAs[Assignee]("assignedto").get,
        doc.getAs[TicketStatus]("status").get,
        doc.getAs[Category]("category").get)
  }
}
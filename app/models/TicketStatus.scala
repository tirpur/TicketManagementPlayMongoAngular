package models



import play.api.libs.json.Json
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer

/*
 * Created by shri on 14/12/15.
 */

case class TicketStatus(name: String, code: Int)

object TicketStatus {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val statusFormat = Json.format[TicketStatus]

  /** serialize a Name into a BSON */
  implicit object CSRBSONWriter extends BSONDocumentWriter[TicketStatus] {
    def write(state: TicketStatus): BSONDocument =
      BSONDocument(
        "name" -> state.name,
        "code" -> state.code)
  }

  /** deserialize a Name from a BSON */
  implicit object CSRBSONReader extends BSONDocumentReader[TicketStatus] {
    def read(doc: BSONDocument): TicketStatus =
      TicketStatus(
        doc.getAs[String]("name").get,
        doc.getAs[Int]("code").get)
  }
}

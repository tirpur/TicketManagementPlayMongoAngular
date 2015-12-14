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
 * Author: Shri
 */

case class Assignee(name: String, email: String)

object Assignee {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val assigneeFormat = Json.format[Assignee]

  /** serialize a Name into a BSON */
  implicit object AssigneeBSONWriter extends BSONDocumentWriter[Assignee] {
    def write(asgn: Assignee): BSONDocument =
      BSONDocument(
        "name" -> asgn.name,
        "email" -> asgn.email)
  }

  /** deserialize a Name from a BSON */
  implicit object AssigneeBSONReader extends BSONDocumentReader[Assignee] {
    def read(doc: BSONDocument): Assignee =
      Assignee(
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get)
  }
}
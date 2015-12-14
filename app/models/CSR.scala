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

case class CSR(name: String, email: String)

object CSR {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val csrFormat = Json.format[CSR]

  /** serialize a Name into a BSON */
  implicit object CSRBSONWriter extends BSONDocumentWriter[CSR] {
    def write(csr: CSR): BSONDocument =
      BSONDocument(
        "name" -> csr.name,
        "email" -> csr.email)
  }

  /** deserialize a Name from a BSON */
  implicit object CSRBSONReader extends BSONDocumentReader[CSR] {
    def read(doc: BSONDocument): CSR =
      CSR(
        doc.getAs[String]("name").get,
        doc.getAs[String]("email").get)
  }
}
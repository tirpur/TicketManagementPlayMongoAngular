
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

case class Category(name: String, desc: String)

object Category {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val CategoryFormat = Json.format[Category]

  /** serialize a Name into a BSON */
  implicit object CategoryBSONWriter extends BSONDocumentWriter[Category] {
    def write(asgn: Category): BSONDocument =
      BSONDocument(
        "name" -> asgn.name,
        "desc" -> asgn.desc)
  }

  /** deserialize a Name from a BSON */
  implicit object CategoryBSONReader extends BSONDocumentReader[Category] {
    def read(doc: BSONDocument): Category =
      Category(
        doc.getAs[String]("name").get,
        doc.getAs[String]("desc").get)
  }
}
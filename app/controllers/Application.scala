package controllers

import java.io.File

import play.api.Play
import play.api.Play._

import play.api.mvc._

//class Application extends Controller {
//
//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }
//
//}
class Application extends Controller {

  /** serve the index page app/views/index.scala.html */
  def index(any: String) = Action {
    Ok(views.html.index())
  }

  /** resolve "any" into the corresponding HTML page URI */
  def getURI(any: String): String = any match {
    case "main" => "/public/html/main.html"
    case "detail" => "/public/html/detail.html"
    case _ => "error"
  }

  /** load an HTML page from public/html */
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application.path.toPath.toString
    var file = new File(projectRoot + getURI(any))
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }
}
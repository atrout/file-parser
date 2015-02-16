package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{Json, Writes}

import parsers.FileParser

case class ParsedFile(file: String, count: Int, words: Map[String, Int])

object Application extends Controller {

  // JSON writer for parsed file data
  implicit val parsedFileWrites = new Writes[ParsedFile] {
    def writes(parsedFile: ParsedFile) = Json.obj(
      "parsed_file" -> parsedFile.file,
      "total_word_count" -> parsedFile.count,
      "word_occurrences" -> parsedFile.words.map {case (word, count) =>
          Json.obj(
            "word" -> word,
            "count" -> count
          )
        }
    )
  }

  // returns a user interface if requester accepts html
  // otherwise returns status as JSON
  def index = Action { request =>
    request match {
      case Accepts.Html() => Ok(views.html.index())
      case Accepts.Json() => Ok(Json.obj("status" -> "success", "service" -> "file upload and parse"))
    }
  }

  // returns JSON data about the parsed file
  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("textFile").map { textFile => 
      import java.io.File
      val filename = textFile.filename
      val contentType = textFile.contentType.get

      // temporarily write the file to the 'target' directory
      val tempFileName = "target/" + textFile.filename
      val file = new File(tempFileName)
      textFile.ref.moveTo(file)
      
      val (count, wordMap) = FileParser.getWordCount(tempFileName)

      // delete temporary file
      file.delete()
      
      request match {
        case Accepts.Html() => 
          val unique = wordMap.size
          val message = s"The file $filename contains a total of $count words and $unique distinct words."
          Ok(views.html.result(message, wordMap))
        case Accepts.Json() => 
          val parsed = ParsedFile(filename, count, wordMap)
          Ok(Json.obj("status" -> "success", "data" -> parsed))
      }
      
    }.getOrElse {
      request match {
        case Accepts.Html() =>
          Redirect(routes.Application.index).flashing(
            "error" -> "Missing file")
        case Accepts.Json() =>
          Ok(Json.obj("status" -> "fail", "data" -> Json.obj("file" -> "a file is required")))          
      }
      
    }
  }

}
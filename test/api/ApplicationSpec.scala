import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.{ContentBody, FileBody}

import java.io.{ByteArrayOutputStream, File}
import scala.io.Source.fromFile

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.Files._
import play.api.mvc.{AnyContentAsMultipartFormData, MultipartFormData}
import play.api.mvc.MultipartFormData.{BadPart, FilePart, MissingFilePart}
import play.api.http.Writeable


@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification {

  val testFile = "test/resources/wordfile.txt"
  
  // this was taken from a test I found on github:
  // https://github.com/G-Node/GCA-Web/blob/master/test/controller/FigureCtrlTest.scala
  // AnyContentAsMultipartFormData needs to be converted to Bytes for the upload test to work
  implicit def writeableOf_AnyContentAsMultipartFormData:
    Writeable[AnyContentAsMultipartFormData] = {
      val boundary = "---XXX---"
      val charset = "UTF-8"
      
      Writeable(dataParts => {
        

        val text: Iterable[Array[Byte]] = for (dp <- dataParts.mdf.dataParts) yield {
          val header = ("Content-Disposition: form-data; name=\"" + dp._1 + "\"\r\n\r\n").getBytes(charset)
          val data = dp._2.mkString.getBytes(charset)
          val footer = "\r\n".getBytes(charset)

          header ++ data ++ footer
        }

        val data: Iterable[Array[Byte]] = for (file <- dataParts.mdf.files) yield {
          val header = ("Content-Disposition: form-data; name=\"" + file.key +
            "\"; filename=\"" + file.filename + "\"\r\n" + "Content-Type: " +
            file.contentType.getOrElse("text/plain") + "\r\n\r\n").getBytes(charset)
          val data = fromFile(file.ref.file).map(_.toByte).toArray
          val footer = "\r\n".getBytes(charset)

          header ++ data ++ footer
        }

        val separator = ("--" + boundary + "\r\n").getBytes(charset)
        val body = (text ++ data).foldLeft(Array[Byte]())((r,c) => r ++ separator ++ c)

        body ++ separator ++ "--".getBytes(charset)

      }, Some(s"multipart/form-data; boundary=$boundary"))
  }

  "Application" should {

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Upload a file")
    }

    "return json for get request if headers request json" in new WithApplication {
      val home = route(FakeRequest(GET, "/").withHeaders(("Accept", "application/json"))).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain ("file upload and parse")
    }

    // upload endpoint
    "allow a file upload" in new WithApplication {

      val file = new File(testFile)
      val tempFile = TemporaryFile(file)
      val part = FilePart("textFile", "wordfile.txt", Some("text/plain"), tempFile)
      val files = Seq[FilePart[TemporaryFile]](part)
      val multipartBody = MultipartFormData(Map[String, Seq[String]](), files, Seq[BadPart](), Seq[MissingFilePart]())
      val request = FakeRequest(POST, "/").withMultipartFormDataBody(multipartBody)

      val upload = route(request).get

      status(upload) must equalTo(OK)
    }

    // bad request
    "return an error if no file is uploaded" in new WithApplication {
      val upload = route(FakeRequest(POST, "/")).get

      status(upload) must equalTo(400)
      contentAsString(upload) must contain("Bad Request:");
    }

  }
}

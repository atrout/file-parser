package parsers

import org.specs2.mutable.Specification

class FileParsersSpec extends Specification {

  val testFile = "resources/wordfile.txt"

  "getWordCount" should {

    "parse an empty file" in {
      FileParser.getWordCount("") must equalTo (0, Map.empty)
    }

  }
}

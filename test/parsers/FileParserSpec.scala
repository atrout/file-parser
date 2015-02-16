package parsers

import org.specs2.mutable.Specification

class FileParsersSpec extends Specification {

  val testFile = "test/resources/wordfile.txt"

  "getWordCount" should {

    "parse a file and return a wordcount" in {
      var (count, wordMap) = FileParser.getWordCount(testFile)
      count must_== 10
      wordMap.keys must contain("apple")
    }

    "throw an exception if there is no file to parse" in {
      FileParser.getWordCount("") must throwA[Exception]
    }

  }
}

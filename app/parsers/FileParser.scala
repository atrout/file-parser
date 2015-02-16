package parsers

import scala.io.Source

trait FileParser {

  // reads a file and returns total word count
  // and a map of strings (words that occur in the file)
  // and a count corresponding to each word
  def getWordCount(file: String): (Int, Map[String, Int]) = {
    val words = Source.fromFile(file)
      .getLines
      .flatMap(_.split("\\W+"))
      .filter(! _.isEmpty)
      .toVector

    val wordOccurrences = words.foldLeft(Map.empty[String, Int]) {
      (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
    }
      
    val size = words.size

    return (size, wordOccurrences)
  }
  
}

object FileParser extends FileParser
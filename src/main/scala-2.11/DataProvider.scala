import scala.collection.mutable.ArrayBuffer
import com.typesafe.scalalogging.LazyLogging

object DataProvider extends LazyLogging {

  def readCSV(filename: String, printToConsole: Boolean = false): Array[Array[String]] = {
    logger.info("Read file '/resources" + filename + "'")
    val rows = ArrayBuffer[Array[String]]()
    // (1) read the csv data
    using(io.Source.fromURL(getClass.getResource(filename))) { source =>
      for (line <- source.getLines.drop(1)) {
        rows += line.split(",").map(_.trim)
      }
    }
    // (2) print the results
    printToConsole match {
      case true => for (row <- rows)
        println(s"${row(0)},${row(1)},${row(2)},${row(3)},${row(4)},${row(5)},${row(6)},${row(7)}")
      case false => ()
    }
    rows.toArray
  }

  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }
}

import com.typesafe.scalalogging.LazyLogging
import scala.collection.mutable.ArrayBuffer

object Main extends App with LazyLogging {
  logger.warn("Hello Scala and SBT world :)");
  val rows = ArrayBuffer[Array[String]]()

  // (1) read the csv data
  using(io.Source.fromURL(getClass.getResource("/palnes_log.csv"))) { source =>
    for (line <- source.getLines.drop(1)) {
      rows += line.split(",").map(_.trim)
    }
  }

  // (2) print the results
  for (row <- rows) {
    println(s"${row(0)}|${row(1)}|${row(2)}|${row(3)}|${row(4)}|${row(5)}|${row(6)}|${row(7)}")
  }

  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
import java.text.SimpleDateFormat
import java.util.Calendar
import java.io.PrintWriter
import com.typesafe.scalalogging.LazyLogging
import scala.collection.mutable.ArrayBuffer

object Main extends App with LazyLogging {
  logger.warn("Hello Scala and SBT world :)");
  lazy val cvsData = readCSV("/palnes_log.csv");
  //lazy val cvsData = readCSV("/palnes_log_full.csv");
  val step01 = stepFirst(cvsData)
  val step02 = stepSecond(cvsData)
  val step03 = stepThird(cvsData)

  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }

  private def readCSV(filename: String): Array[Array[String]] = {
    logger.info("Read file '/resources" + filename + "'")
    val rows = ArrayBuffer[Array[String]]()
    // (1) read the csv data
    using(io.Source.fromURL(getClass.getResource(filename))) { source =>
      for (line <- source.getLines.drop(1)) {
        rows += line.split(",").map(_.trim)
      }
    }
    // (2) print the results
    for (row <- rows) {
      println(s"${row(0)},${row(1)},${row(2)},${row(3)},${row(4)},${row(5)},${row(6)},${row(7)}")
    }
    rows.toArray
  }

  private lazy val airports: Array[String] = {
    val airports = (cvsData.groupBy(_(7)).keySet ++ cvsData.groupBy(_(6)).keySet)
    airports.toArray
  }

  def stepFirst(data: Array[Array[String]]): Array[(String, Int)] = {
    logger.info("First task")
    logger.debug("> function 'stepFirst' START")
    val step1result = airports.map(airport => (airport, data.count(_(7) == airport)))
    //printTuple2(step1result)
    writeToFile(step1result, "first.csv")
    logger.debug("< function 'stepFirst' END")
    step1result
  }

  def stepSecond(data: Array[Array[String]]): Array[(String, Int)] = {
    logger.info("Second task")
    logger.debug("> function 'stepSecond' START")
    val origin = data.groupBy(_ (6)) mapValues (_.size)
    val dest: scala.collection.immutable.Map[String, Int] = data.groupBy(_ (7)) mapValues (_.size)
    val step2result = airports.map(key => (key, dest.getOrElse(key, 0) - origin.getOrElse(key, 0))).filter(_._2 != 0)
    //printTuple2(step2result)
    writeToFile(step2result, "second.csv")
    logger.debug("< function 'stepSecond' END")
    step2result
  }

  def stepThird(data: Array[Array[String]]): Map[Int, Array[(String, Int)]] = {
    logger.info("Third task")
    logger.debug("> function 'stepThird' START")
    val pairWeekDest = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7)))
    val grouped: scala.collection.immutable.Map[Int, Array[(Int, String)]] = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7))).groupBy(_._1)
    val sGrp = scala.collection.immutable.TreeMap(grouped.toArray:_*)
    val groupedPerWeekOfYear: scala.collection.immutable.Map[Int,Array[(String, Int)]] = sGrp.flatMap(week => Map(week._1 -> airports.map(airport => (airport, week._2.count((_)._2 == airport) ))))
    //print result
    /*for ((weekNum, values) <- groupedPerWeekOfYear) {
      println("W" +weekNum)
      for (airportsArray <- values) {
        println("  " +airportsArray._1 + ": " + airportsArray._2)
      }
    }*/
    writeToTextFile(groupedPerWeekOfYear, "third.txt")
    logger.debug("< function 'stepThird' END")
    groupedPerWeekOfYear
  }

  private def printTuple2[A, B](input: Array[Tuple2[A, B]]): Unit = {
    for (row <- input) {
      println("  " + row._1 + " " + row._2)
    }
  }

  private def writeToFile[A, B](input: Array[Tuple2[A, B]], filename: String): Unit = {
    val out = new PrintWriter("src/main/resources/results/" + filename)
    for (row <- input) {
      out.println(row._1 + "," + row._2 + ",")
    }
    out.close()
  }

  private def writeToTextFile(input: Map[Int,Array[(String, Int)]], filename: String): Unit = {
    val out = new PrintWriter("src/main/resources/results/" + filename)
    for ((weekNum, values) <- input) {
      out.println("W" +weekNum)
      for (airportsArray <- values) {
        out.println("  " +airportsArray._1 + ": " + airportsArray._2)
      }
    }
    out.close()
  }

  private def extractWeek(year: String, month: String, day: String): Int = {
    val c = Calendar.getInstance();
    val df = new SimpleDateFormat("yyyy-MM-dd");
    val date = df.parse(year + "-" + month + "-" + day)
    c.setTime(date)
    val week = c.get(Calendar.WEEK_OF_YEAR);
    week
  }
}
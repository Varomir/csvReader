import java.text.SimpleDateFormat
import java.util.Calendar
import java.io.PrintWriter
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
  val cvsData = rows.toArray

  // (2) print the results
  for (row <- cvsData) {
    println(s"${row(0)},${row(1)},${row(2)},${row(3)},${row(4)},${row(5)},${row(6)},${row(7)}")
  }
  val step01 = stepFirst(cvsData)
  val step02 = stepSecond(cvsData)
  val step03 = stepThree(cvsData)

  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def stepFirst(data: Array[Array[String]]): Array[(String, Int)] = {
    val step1result = airports.map(airport => (airport, data.count(_(7) == airport)))
    println("First task")
    printTuple2(step1result)
    writeToFile(step1result, "first.csv")
    step1result
  }

  def stepSecond(data: Array[Array[String]]): Array[(String, Int)] = {
    println("Second task")
    val origin = data.groupBy(_ (6)) mapValues (_.size)
    val dest: scala.collection.immutable.Map[String, Int] = data.groupBy(_ (7)) mapValues (_.size)
    val step2result = airports.map(key => (key, dest.getOrElse(key, 0) - origin.getOrElse(key, 0))).filter(_._2 != 0)
    printTuple2(step2result)
    writeToFile(step2result, "second.csv")
    step2result
  }

  def stepThree(data: Array[Array[String]]): Map[Int, Array[(String, Int)]] = {
    println("Third task")
    val pairWeekDest = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7)))
    val grouped: scala.collection.immutable.Map[Int, Array[(Int, String)]] = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7))).groupBy(_._1)
    val sGrp = scala.collection.immutable.TreeMap(grouped.toArray:_*)
    val groupedPerWeekOfYear: scala.collection.immutable.Map[Int,Array[(String, Int)]] = sGrp.flatMap(week => Map(week._1 -> airports.map(airport => (airport, week._2.count((_)._2 == airport) ))))
    //print result
    for ((weekNum, values) <- groupedPerWeekOfYear) {
      println("W" +weekNum)
      for (airportsArray <- values) {
        println("  " +airportsArray._1 + ": " + airportsArray._2)
      }
    }
    writeToTextFile(groupedPerWeekOfYear, "third.txt")
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
      out.println(row._1 + "," + row._2)
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

  private def airports: Array[String] = {
    val airports = (rows.groupBy(_(7)).keySet ++ rows.groupBy(_(6)).keySet)
    airports.toArray
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
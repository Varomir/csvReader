import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Calendar

import com.typesafe.scalalogging.LazyLogging

object Statistic extends LazyLogging {

  private def getAirports(csvData: Array[Array[String]]): Array[String] = {
    val airports = (csvData.groupBy(_(7)).keySet ++ csvData.groupBy(_(6)).keySet)
    airports.toArray
  }

  def stepFirst(data: Array[Array[String]], printToConsole: Boolean = false): Array[(String, Int)] = {
    logger.info("First task")
    logger.debug("> function 'stepFirst' START")
    val step1result = getAirports(data).map(airport => (airport, data.count(_(7) == airport)))
    printTuple2(step1result, printToConsole)
    writeToFile(step1result, "first.csv")
    logger.debug("< function 'stepFirst' END")
    step1result
  }

  def stepSecond(data: Array[Array[String]], printToConsole: Boolean = false): Array[(String, Int)] = {
    logger.info("Second task")
    logger.debug("> function 'stepSecond' START")
    val origin = data.groupBy(_ (6)) mapValues (_.size)
    val dest: scala.collection.immutable.Map[String, Int] = data.groupBy(_ (7)) mapValues (_.size)
    val step2result = getAirports(data).map(key => (key, dest.getOrElse(key, 0) - origin.getOrElse(key, 0))).filter(_._2 != 0)
    printTuple2(step2result, printToConsole)
    writeToFile(step2result, "second.csv")
    logger.debug("< function 'stepSecond' END")
    step2result
  }

  def stepThird(data: Array[Array[String]], printToConsole: Boolean = false): Map[Int, Array[(String, Int)]] = {
    logger.info("Third task")
    logger.debug("> function 'stepThird' START")
    val pairWeekDest = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7)))
    val grouped: scala.collection.immutable.Map[Int, Array[(Int, String)]] = data.map(row => (extractWeek(row(0), row(2), row(3)), row(7))).groupBy(_._1)
    val sGrp = scala.collection.immutable.TreeMap(grouped.toArray:_*)
    val groupedPerWeekOfYear: scala.collection.immutable.Map[Int,Array[(String, Int)]] = sGrp.flatMap(week => Map(week._1 -> getAirports(data).map(airport => (airport, week._2.count((_)._2 == airport) ))))
    //print result
    printToConsole match {
      case true => for ((weekNum, values) <- groupedPerWeekOfYear) {
        println ("W" + weekNum)
        for (airportsArray <- values) {
          println ("  " + airportsArray._1 + ": " + airportsArray._2)
        }
      }
      case false => ()
    }
    writeToTextFile(groupedPerWeekOfYear, "third.txt")
    logger.debug("< function 'stepThird' END")
    groupedPerWeekOfYear
  }

  private def printTuple2[A, B](input: Array[Tuple2[A, B]], print: Boolean): Unit = {
    print match {
      case true => for (row <- input) {
        println("  " + row._1 + " " + row._2)
      }
      case false => ()
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

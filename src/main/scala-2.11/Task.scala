import com.typesafe.scalalogging.LazyLogging
import DataProvider.readCSV
import Statistic._

object Task extends App with LazyLogging {
  logger.warn("Hello Scala and SBT world :)")
  //lazy val csvData = readCSV("/palnes_log.csv", true);  val printResult = true
  lazy val csvData = readCSV("/palnes_log_full.csv");   val printResult = false

  val step01 = stepFirst(csvData, printResult)
  val step02 = stepSecond(csvData, printResult)
  val step03 = stepThird(csvData, printResult)
}
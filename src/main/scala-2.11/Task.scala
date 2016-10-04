import com.typesafe.scalalogging.LazyLogging
import DataProvider.readCSV
import Statistic._

object Main extends App with LazyLogging {
  logger.warn("Hello Scala and SBT world :)")
  //lazy val csvData = readCSV("/palnes_log.csv", true)
  lazy val csvData = readCSV("/palnes_log_full.csv")

  val step01 = stepFirst(csvData)
  val step02 = stepSecond(csvData)
  val step03 = stepThird(csvData)
}
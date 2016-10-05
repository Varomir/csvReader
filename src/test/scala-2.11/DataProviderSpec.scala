import org.specs2._

class DataProviderSpec extends Specification { def is = s2"""

 This is a specification to check the 'DataProvider.readCSV' function

 The input file 'palnes_log.csv' converted to two-dimensional array
   array dimension should be equal to 6 x 8                      $e1
                                                                 """


  val dim = DataProvider.readCSV("/palnes_log.csv", false)

  def e1 = (dim must have size(6)) and (dim(5) must have size (8))
}

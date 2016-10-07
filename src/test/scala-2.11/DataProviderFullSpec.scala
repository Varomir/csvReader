import org.specs2._

class DataProviderFullSpec extends Specification { def is = s2"""

 This is a specification to check the 'DataProvider.readCSV' function

 The input file 'palnes_log_full.csv' converted to two-dimensional array
   array dimension should be equal to 471949 x 8                 $e1
   first line 'ORIGIN' value should be 'JFK'                     $e2
   second line 'DEST' value should not start with 'JFK'          $e3
   last line 'FL_DATE' value should be '2014-01-31'              $e4
                                                                 """

  val dim = DataProvider.readCSV("/palnes_log_full.csv", false)

  def e1 = (dim must be size(471949)) and (dim(5) must have size (8)) //dangerous test! very big expected collections size
  def e2 = dim(0)(6) must beMatching(""""JFK"""")
  def e3 = dim(1)(7) must not startWith(""""JFK"""")
  def e4 = dim(471948)(5) must be_== ("2014-01-31")
}

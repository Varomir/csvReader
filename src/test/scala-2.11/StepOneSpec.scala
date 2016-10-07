import org.specs2.Specification

class StepOneSpec extends Specification { def is = s2"""

  This is my specification for 'Statistic.stepFirst' function
    List of all airports with total number of planes for the whole period that arrived to each airport should:
      have size 3                     $e1
      contains pair(JFK,0)            $e2
      contains pair(KBP,1)            $e3
      contains pair(LAX,2)            $e4
      not contains pair(JFK,2)        $e5
      not contains pair(LAX,1)        $e6
                                  """

  val testCSV_Data = Array(
    Array("0","0","0","0","0","0","JFK","LAX"),
    Array("0","0","0","0","0","0","LAX","KBP"),
    Array("0","0","0","0","0","0","JFK","LAX")
  )
  val printResult = true
  val result: Array[(String, Int)] = Statistic.stepFirst(testCSV_Data, printResult)

  def e1 = result must have size(3)
  def e2 = result.toList must contain(("JFK" -> 0))
  def e3 = result.toList must contain(("KBP" -> 1))
  def e4 = result.toList must contain(("LAX" -> 2))
  def e5 = result.toList must not contain(("JFK" -> 2))
  def e6 = result.toList must not contain(("LAX" -> 1))

}

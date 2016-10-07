import org.specs2.Specification

class StepTwoSpec extends Specification { def is = s2"""

  This is my specification for 'Statistic.stepSecond' function
    Non-Zero difference in total number of planes that arrived to and left from the airport should:
      have size 3                     $e1
      contains pair(JFK,-3)           $e2
      contains pair(LAX,2)            $e3
      contains pair(KBP,1)            $e4
      not contains pair(AAA,0)        $e5
                                """


  val testCSV_Data = Array(
    Array("0","0","0","0","0","0","JFK","LAX"),
    Array("0","0","0","0","0","0","LAX","KBP"),
    Array("0","0","0","0","0","0","JFK","AAA"),
    Array("0","0","0","0","0","0","JFK","LAX"),
    Array("0","0","0","0","0","0","AAA","LAX")
  )
  val printResult = true
  val result: Array[(String, Int)] = Statistic.stepSecond(testCSV_Data, printResult)

  def e1 = result must have size(3)
  def e2 = result.toList must contain(("JFK" -> -3))
  def e3 = result.toList must contain(("LAX" -> 2))
  def e4 = result.toList must contain(("KBP" -> 1))
  def e5 = result.toList must not contain(("AAA" -> 0))
}

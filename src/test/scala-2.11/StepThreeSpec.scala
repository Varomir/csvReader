import org.specs2.Specification

class StepThreeSpec extends Specification { def is = s2"""

  This is my specification for 'Statistic.stepThird' function
    List of all airports with total number of planes but sum number of planes separately per each week should:
      have size 2                                         $e1
      have not key 1                                      $e2
      have key 2                                          $e3
      have key 3                                          $e4
      have not key 4                                      $e5
      key(2) must have size 3                             $e6
      key(3) must have size 3                             $e7
      key(2) must contain ("KBP",1),("LAX",3),("JFK",0)   $e8
      key(3) must contain ("KBP",1),("LAX",1),("JFK",0)   $e9
                                """

  val testCSV_Data = Array(
    Array("2014","0","1","7","0","0","JFK","LAX"),
    Array("2014","0","1","5","0","0","JFK","KBP"),
    Array("2014","0","1","6","0","0","KBP","LAX"),
    Array("2014","0","1","8","0","0","JFK","LAX"),
    Array("2014","0","1","12","0","0","JFK","KBP"),
    Array("2014","0","1","13","0","0","KBP","LAX")
  )

  val printResult = true
  val result = Statistic.stepThird(testCSV_Data, printResult)

  def e1 = result must have size(2)
  def e2 = result must not have key(1)
  def e3 = result must have key(2)
  def e4 = result must have key(3)
  def e5 = result must not have key(4)
  def e6 = result(2) must have size(3)
  def e7 = result(3) must have size(3)
  def e8 = result(2).toList must contain(("KBP" -> 1), ("LAX" -> 3), ("JFK" -> 0))
  def e9 = result(3).toList must contain(("KBP" -> 1), ("LAX" -> 1), ("JFK" -> 0))
}

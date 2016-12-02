/**
 * 
 */


import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2011
 */
public class TestCases extends CyclicIterator<Map<String, Angled>> {

  Map<String, Angled> stop() {
    return this.stop;
  }

  private final Map<String, Angled> stop;

  @SuppressWarnings("unchecked")
  TestCases() {
    this.stop = new HashMap<String, Angled>();
    final Map<String, Angled> peace = new HashMap<String, Angled>();
    final Map<String, Angled> fist = new HashMap<String, Angled>();
    final Map<String, Angled> shaka = new HashMap<String, Angled>();
    final Map<String, Angled> spread = new HashMap<String, Angled>();
    final Map<String, Angled> claw = new HashMap<String, Angled>();

    super.add(stop, peace, fist, shaka, spread, claw);

    // the upper arm, forearm, and hand angles do not change through any of the
    // test cases
    stop.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));
    peace.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));
    fist.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));
    shaka.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));
    spread.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));
    claw.put(PA2.ABDOMEN_NAME, new BaseAngled(0, 0, 0));

    stop.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));
    peace.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));
    fist.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));
    shaka.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));
    spread.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));
    claw.put(PA2.THROAT_NAME, new BaseAngled(0, 90, 0));

    stop.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));
    peace.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));
    fist.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));
    shaka.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));
    spread.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));
    claw.put(PA2.HEAD_NAME, new BaseAngled(0, 0, 0));

    // the stop test case
    stop.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_BODY_FOUR, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_DISTAL_TWO, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_BODY_TWO, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_DISTAL_THREE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_BODY_THREE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_DISTAL_ONE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_BODY_ONE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(0, 0, 0));
    stop.put(PA2.LEG_BODY_FIVE, new BaseAngled(0, 50, -60));

    // the peace sign test case
    peace.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(50, 0, 0));
    peace.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(90, 0, 0));
    peace.put(PA2.LEG_BODY_FOUR, new BaseAngled(60, 0, 0));
    peace.put(PA2.LEG_DISTAL_TWO, new BaseAngled(50, 0, 0));
    peace.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(90, 0, 0));
    peace.put(PA2.LEG_BODY_TWO, new BaseAngled(60, 0, 0));
    peace.put(PA2.LEG_DISTAL_THREE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_BODY_THREE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_DISTAL_ONE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_BODY_ONE, new BaseAngled(0, 0, 0));
    peace.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(10, 0, 0));
    peace.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(90, 0, 0));
    peace.put(PA2.LEG_BODY_FIVE, new BaseAngled(30, 50, -60));

    // the fist test case
    fist.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(50, 0, 0));
    fist.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(90, 0, 0));
    fist.put(PA2.LEG_BODY_FOUR, new BaseAngled(60, 0, 0));
    fist.put(PA2.LEG_DISTAL_TWO, new BaseAngled(50, 0, 0));
    fist.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(90, 0, 0));
    fist.put(PA2.LEG_BODY_TWO, new BaseAngled(60, 0, 0));
    fist.put(PA2.LEG_DISTAL_THREE, new BaseAngled(50, 0, 0));
    fist.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(90, 0, 0));
    fist.put(PA2.LEG_BODY_THREE, new BaseAngled(60, 0, 0));
    fist.put(PA2.LEG_DISTAL_ONE, new BaseAngled(50, 0, 0));
    fist.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(90, 0, 0));
    fist.put(PA2.LEG_BODY_ONE, new BaseAngled(60, 0, 0));
    fist.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(50, 0, 0));
    fist.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(60, 0, 0));
    fist.put(PA2.LEG_BODY_FIVE, new BaseAngled(30, 50, -60));

    // the shaka test case
    shaka.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(0, 0, 0));
    shaka.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(0, 0, 0));
    shaka.put(PA2.LEG_BODY_FOUR, new BaseAngled(0, -15, 0));
    shaka.put(PA2.LEG_DISTAL_TWO, new BaseAngled(50, 0, 0));
    shaka.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(80, 0, 0));
    shaka.put(PA2.LEG_BODY_TWO, new BaseAngled(45, 0, 0));
    shaka.put(PA2.LEG_DISTAL_THREE, new BaseAngled(50, 0, 0));
    shaka.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(80, 0, 0));
    shaka.put(PA2.LEG_BODY_THREE, new BaseAngled(45, 0, 0));
    shaka.put(PA2.LEG_DISTAL_ONE, new BaseAngled(50, 0, 0));
    shaka.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(80, 0, 0));
    shaka.put(PA2.LEG_BODY_ONE, new BaseAngled(45, 0, 0));
    shaka.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(-10, 0, 0));
    shaka.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(0, 0, 0));
    shaka.put(PA2.LEG_BODY_FIVE, new BaseAngled(0, 60, -60));

    // the spread test case
    spread.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_BODY_FOUR, new BaseAngled(0, -10, 0));
    spread.put(PA2.LEG_DISTAL_TWO, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_BODY_TWO, new BaseAngled(0, -7, 0));
    spread.put(PA2.LEG_DISTAL_THREE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_BODY_THREE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_DISTAL_ONE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_BODY_ONE, new BaseAngled(0, 10, 0));
    spread.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(0, 0, 0));
    spread.put(PA2.LEG_BODY_FIVE, new BaseAngled(0, 60, -60));

    // the claw test case
    claw.put(PA2.LEG_DISTAL_FOUR, new BaseAngled(60, 0, 0));
    claw.put(PA2.LEG_MIDDLE_FOUR, new BaseAngled(80, 0, 0));
    claw.put(PA2.LEG_BODY_FOUR, new BaseAngled(0, 0, 0));
    claw.put(PA2.LEG_DISTAL_TWO, new BaseAngled(60, 0, 0));
    claw.put(PA2.LEG_MIDDLE_TWO, new BaseAngled(80, 0, 0));
    claw.put(PA2.LEG_BODY_TWO, new BaseAngled(0, 0, 0));
    claw.put(PA2.LEG_DISTAL_THREE, new BaseAngled(60, 0, 0));
    claw.put(PA2.LEG_MIDDLE_THREE, new BaseAngled(80, 0, 0));
    claw.put(PA2.LEG_BODY_THREE, new BaseAngled(0, 0, 0));
    claw.put(PA2.LEG_DISTAL_ONE, new BaseAngled(60, 0, 0));
    claw.put(PA2.LEG_MIDDLE_ONE, new BaseAngled(80, 0, 0));
    claw.put(PA2.LEG_BODY_ONE, new BaseAngled(0, 0, 0));
    claw.put(PA2.LEG_DISTAL_FIVE, new BaseAngled(70, 0, 0));
    claw.put(PA2.LEG_MIDDLE_FIVE, new BaseAngled(20, 0, 0));
    claw.put(PA2.LEG_BODY_FIVE, new BaseAngled(30, 50, -60));
  }
}

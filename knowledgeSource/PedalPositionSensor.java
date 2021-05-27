package knowledgeSource;
import blackboard.Blackboard;

/**
 * @author David Allison
 * Simulates a pedal position sensor. Possible pedal positions: 10%, 20%, .. 100%
 */
public class PedalPositionSensor extends KnowledgeSource {
    /**
     * Constructor
     * @param bb: Blackboard to post knowledge to
     */
    public PedalPositionSensor(Blackboard bb) {
        super(bb);
        super.priority = 6;
        super.sourceType = "GAS";
        currentVal = 0;
    }

    /**
     * Increase pedal position by 10% if less than 100%
     */
    public void accelerate() {
        if (currentVal + 10 <= 100) {
                currentVal += 10;
        } else {
                currentVal = 100;
        }
        super.updateBb();
    }

    /**
     * Decrease pedal position by 10% if greater than 0%
     */
    public void decelerate() {
        if (currentVal - 10 >= 0) {
                currentVal -= 10;
        } else {
                currentVal = 0;
        }
        super.updateBb();
    }

}

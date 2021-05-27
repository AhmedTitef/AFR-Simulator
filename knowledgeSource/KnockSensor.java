package knowledgeSource;
import blackboard.*;
import java.util.Random;

/**
 * @author David Allison
 * Simulates a Knock Sensor. Creates random knock events above AFR = 14.7 and
 * allows for immediate knock events to manually be created.
 * As RPMs rise, chance of a knock event is increased.
 *
 */
public class KnockSensor extends KnowledgeSource implements EngineSource {
    AirFuelRatioSensor afr;
    EngineRPMSensor rpm;

    /**
     * Constructor
     * @param bb: Blackboard to post knowledge to
     * @param afr: AFR sensor to find air fuel ratio from (used in simulated knock events)
     */
    public KnockSensor(Blackboard bb, AirFuelRatioSensor afr, EngineRPMSensor rpm) {
            super(bb);
            super.priority = 1;
            super.sourceType = "KS";
            this.afr = afr;
            this.rpm = rpm;
    }

    /**
     * Inherited from EngineSource, simulates knock events above a given AFR value.
     * Knock events become more likely in a lean (>14.7) AFR and at high RPM.
     */
    public void updateVal() {
    		currentVal = 0;
            if (afr.currentVal > 14.7) { // if AFR is too lean, randomly cause knock events
                    Random rand = new Random();
                    // as RPMs rise, give between a 1/500 chance to a 1/5 chance that knock events will occur
                    int chance = (int)(500 - (495 / rpm.maxRPMs) * rpm.currentVal);
                    currentVal = rand.nextInt(chance);
            }
            if (currentVal != 1.0) {
                    super.priority = 1;
            } else { // if knock event occurred, this is high priority
                    super.priority = 9;
            }
            super.updateBb();
    }

    /**
     * Manually create a simulated knock event.
     * Useful in simulation.
     */
    public void causeDetonationEvent() {
            currentVal = 1;
            super.updateBb();
    }

	
}

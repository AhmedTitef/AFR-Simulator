package knowledgeSource;
import blackboard.*;
import io.*;

/**
 * @author David Allison
 * Simulates an Engine Speed Sensor. For simplicity, calculates an RPM value 
 * corresponding to how far open the throttle body is.
 */
public class EngineRPMSensor extends KnowledgeSource implements EngineSource {
	private Throttle throttle;
	public final double maxRPMs = 7000;

	/**
	 * Constructor
	 * @param bb: Blackboard to post knowledge to
	 * @param throttle: Used to calculate an approximate engine RPM
	 */
	public EngineRPMSensor(Blackboard bb, Throttle throttle) {
            super(bb);
            super.priority = 5;
            super.sourceType = "RPM";
            this.throttle = throttle;
	}

	/**
	 * Inherited from EngineSource, calculates a reasonable RPM value given throttle position.
	 * Converted from double -> int -> double to truncate decimals
	 */
	public void updateVal() {
            currentVal = (double)(int)(((double)throttle.currentVal()/(double)throttle.maxValue) * maxRPMs);
            super.updateBb();
	}
	
}

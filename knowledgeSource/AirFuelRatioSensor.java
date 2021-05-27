package knowledgeSource;
import blackboard.*;
import io.*;

/**
 * @author David Allison
 * Simulates an Air Fuel Ratio Sensor. For simplicity, measures throttle and 
 * injector output values instead of exhaust gases.
 */
public class AirFuelRatioSensor extends KnowledgeSource implements EngineSource {
    private Throttle throttle;
    private FuelInjector injector;

    /**
     * Constructor
     * @param bb: Blackboard to post knowledge to
     * @param throttle: Used to find air volume
     * @param injector: Used to find fuel volume
     */
    public AirFuelRatioSensor(Blackboard bb, Throttle throttle, FuelInjector injector) {
        super(bb);
        super.priority = 5;
        super.sourceType = "AFR";
        this.throttle = throttle;
        this.injector = injector;
    }


    /**
     * Inherited from EngineSource, simulates values coming from an AFR sensor and posts it
     * to the blackboard
     * AFR = Air Volume / Fuel Volume
     */
    public void updateVal() {
        currentVal = (double)throttle.currentVal()/(double)injector.currentVal();
        super.updateBb();
    }
}

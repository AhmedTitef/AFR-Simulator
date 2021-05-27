package io;

/**
 * @author David Allison
 * Simulates a fuel injector capable of maxValue cubic centimeters / minute
 * of fuel flow at full spray.
 */
public class FuelInjector extends OutputComponent {
	public final int maxValue = 500;
	
	/**
	 * @param val: initialize the flow of the fuel injector
	 */
	public FuelInjector(int val) {
            currentVal = val;
	}
	
	/**
	 * Increase the fuel flow through the fuel injector
	 * @param a: Increase fuel flow by this amount
	 *			 If near/at fuel-cut, open all the way
	 */
	public void more(int a) {
            if (currentVal + a < maxValue) {
                    currentVal += a;
            } else {
                    currentVal = maxValue;
            }
	}

	/**
	 * Decrease the fuel flow through the fuel injector
	 * @param a: Decrease fuel flow by this amount
	 *			 If near/at shut-off, close to 1 ccpm (not 0 to avoid undefined AFR)
	 */
	public void less(int a) {
            if (currentVal - a > 1) {
                    currentVal -= a;
            } else {
                    currentVal = 1;
            }
	}


}

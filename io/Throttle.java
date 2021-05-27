package io;

/**
 * @author David Allison
 * Simulates a throttle body capable of maxValue cubic centimeters / minute
 * of air flow at wide open throttle.
 */
public class Throttle extends OutputComponent {
    public final int maxValue = 7500;

    /**
     * @param val: initialize the flow of the throttle body
     */
    public Throttle(int val) {
        currentVal = val;
    }

    /**
     * Increase the air flow through the throttle body
     * @param a: Open the throttle body by this amount
     *			 If near/at wide-open-throttle, open all the way
     */
    public void more(int a) {
        if (currentVal + a <= maxValue) {
                currentVal += a;
        } else {
                currentVal = maxValue;
        }
    }

    /**
     * Decrease the air flow through the throttle body
     * @param a: Close the throttle body by this amount
     *			 If near/at fully closed, close to 1 ccpm
     *			 to avoid a 0 afr feedback loop
     */
    public void less(int a) {
        if (currentVal - a >= 1) {
                currentVal -= a;
        } else {
                currentVal = 1;
        }
    }

}

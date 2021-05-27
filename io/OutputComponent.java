package io;

/**
 * @author David Allison
 * Describes the way an output component such as a fuel injector 
 * or throttle body should behave.
 */
public abstract class OutputComponent {
	protected int currentVal;
	/**
	 * @param a: how much to increase output
	 */
	public abstract void more(int a);
	/**
	 * @param a: how much to decrease output
	 */
	public abstract void less(int a);
	/**
	 * @return the current value of the output
	 */
	public int currentVal() {
            return currentVal;
	}
}

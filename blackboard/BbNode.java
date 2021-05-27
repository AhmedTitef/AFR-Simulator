package blackboard;

/**
 * @author David Allison
 * Container for storing knowledge on a Blackboard.
 */
public class BbNode {
    private int priority;
    private double value;
    private String dataType;

    /**
     * @param p: knowledge priority, tells Controller how important this knowledge is
     * @param v: the knowledge to be passed to Controller
     * @param type: give Controller context as to who wrote this information
     */
    public BbNode(int p, double v, String type) {
        // restrict priorities to 0-10
        if (p > 10 || p < 0) {
            throw new IllegalArgumentException("Priority is not valid!");
        }
        priority = p;
        value = v;
        dataType = type;
    }

    // Getters:

    /**
     * @return this.priority
     */
    public int priority() {
        return priority;
    }

    /**
     * @return this.value
     */
    public double value() {
        return value;
    }

    /**
     * @return this.dataType
     */
    public String type() {
        return dataType;
    }
}

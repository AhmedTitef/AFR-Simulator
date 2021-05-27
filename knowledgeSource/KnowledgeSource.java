package knowledgeSource;
import blackboard.*;

/**
 * @author David Allison
 * Defines structure for a Knowledge Source to be posted to the blackboard.
 * In this implementation, represents one of a car's sensors
 */
public abstract class KnowledgeSource {
    private Blackboard bb;
    protected int priority;
    protected String sourceType;
    protected double currentVal; // uses Double to maximize values that can be conveyed to Blackboard

    /**
     * Constructor
     * @param bb: the blackboard to send knowledge to
     */
    public KnowledgeSource(Blackboard bb) {
            this.bb = bb;
    }

    /**
     * Gather important information from this source and post it to the blackboard
     */
    protected void updateBb() {
            BbNode node = new BbNode(priority, currentVal, sourceType);
            bb.update(node);
    }
}

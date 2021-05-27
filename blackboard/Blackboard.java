package blackboard;
import java.util.HashSet;

/**
 * @author David Allison
 * Shared resource for posting knowledge from knowledge sources.
 * Used in the Blackboard pattern.
 */
public class Blackboard {
    // HashSet allows nodes to be easily differentiated 
    private HashSet<BbNode> nodes;

    /**
     * Initialize nodes HashSet
     */
    public Blackboard() {
        nodes = new HashSet<BbNode>();
    }

    /**
     * @param node: new BbNode to be added to the board
     */
    public void update(BbNode node) {
        nodes.add(node);
    }

    /**
     * @return all knowledge posted to the blackboard, regardless of priority
     */
    public HashSet<BbNode> access() {
        return nodes;
    }

    /**
     * Utility to make sure nodes are removed after the knowledge has been acted upon
     * @param node: node to be removed
     * @return true if successfully removed from blackboard
     */
    public boolean remove(BbNode node) {
        return nodes.remove(node);
    }
}

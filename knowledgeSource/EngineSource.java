package knowledgeSource;

/**
 * @author David Allison
 * Creates a pattern for engine sensors (as opposed to in-vehicle sensors)
 */
public interface EngineSource {
    /**
     * Set the value to be returned by this engine sensor
     */
    public void updateVal();
}

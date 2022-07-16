import vmthread.VMThread;

/**
 * Interface for scheduler classes.
 * 
 * @author Tim
 * @version Feb 23, 2008
 */
public interface IScheduler
{
    /**
     * Returns the thread that will execute an instruction during the
     * next time slice.
     * 
     * @return the thread
     */
    VMThread getCurrentThread();
    
    /**
     * Executes the given number of instructions.
     * 
     * @param numInstructions
     */
    void run(int numInstructions);
}

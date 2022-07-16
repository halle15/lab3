import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import vmthread.VMThread;
import vmthread.VMThreadState;

public class CoopScheduler implements IScheduler
{
	Queue<VMThread> list;
	LinkedList<VMThread> dead = new LinkedList<VMThread>();
	List<SleepyThread> sleep = new LinkedList<SleepyThread>();
	
	private void toEnd() 
	{
		VMThread a = list.poll();
		list.add(a);
	}
	
	private void toDead() 
	{
		VMThread a = list.poll();
		dead.add(a);
	}
	
	private void toSleep(int time) 
	{
		VMThread a = list.poll();
		sleep.add(new SleepyThread(time, a));
	}
	
	private void toWake(int ind) 
	{
		VMThread a = sleep.get(ind).t;
		//sleep.remove(ind);
		list.offer(a);
	}
	
	private void sleepCheck()
	{
		int i = 0;
		if(!sleep.isEmpty()) {
		for(SleepyThread s : sleep) {
			
			if(s.napTime == 0) {
				toWake(i);
				s.napTime--;
			}
			else {
			s.napTime--;
			}
			i++;
			
		}
		}
	}
	
    /**
     * Creates a CoopScheduler.
     * 
     * @param list the list of threads to be executed
     */
    public CoopScheduler(List<VMThread> list)
    {
    	this.list = new LinkedList<VMThread>();
    	for(VMThread v : list) {
    		this.list.add(v);
    	}
    }

    /**
     * Returns the thread that will be executed in the
     * next time slice.
     * 
     * @see IScheduler#getCurrentThread()
     * @return the current thread
     */
    @Override
    public VMThread getCurrentThread()
    {
        return list.peek();
    }

    /**
     * Executes the given number of instructions.
     * 
     * @see IScheduler#run(int)
     * @param numInstructions
     */
    public void run(int numInstructions)
    {
    	for(int i = 0; i < numInstructions; i++) {
    		
    		sleepCheck();
    		tick();
    		
    	}
    }
    
    private void tick() {
    	
    	VMThread thread = getCurrentThread();
    	if (thread == null) {
    		return;
    	}
    	VMThreadState state = thread.runInstruction();	
    	switch (state.getState()) {
    	case Running:
    		break;
    	case Yielded:
    		toEnd();
    		break;
    	case Complete:
    		toDead();
    		break;
    	case Sleeping:
    		toSleep(state.getTicksToSleep());
    		break;
    	}

    }
}

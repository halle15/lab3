import java.util.List;
import java.util.PriorityQueue;
import java.sql.Time;
import java.util.LinkedList;
import vmthread.VMThread;
import vmthread.VMThreadState;


public class PriorityScheduler implements IScheduler
{
    PriorityQueue<ThreadPriority> list = new PriorityQueue<ThreadPriority>();
    LinkedList<ThreadPriority> dead = new LinkedList<ThreadPriority>();
    List<SleepyThread> sleep = new LinkedList<SleepyThread>();
    
    int curPri;
	
    
    private void toEnd() 
	{
		ThreadPriority a = list.poll();
		list.add(a);
		
	}
    
    private void toDead() {
		ThreadPriority a = list.poll();
		dead.add(a);
	}
    
    private void toSleep(int time) {
    	ThreadPriority p = list.poll();
    	sleep.add(new SleepyThread(time, p.t, p.getPriority()));
    }
    
    private void toWake(SleepyThread t) {
    	list.offer(new ThreadPriority(t.t));
    	sleep.remove(t);
    }
    
	private void sleepCheck()
	{
		int i = 0;
		if(!sleep.isEmpty()) {
		for(SleepyThread s : sleep) {
			
			if(s.napTime == 0) {
				toWake(s);
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
    public PriorityScheduler(List<VMThread> list)
    {
    	
    	for(VMThread v : list) {
    		this.list.offer(new ThreadPriority(v));
    	}
    	
    	curPri = this.list.peek().getPriority();
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

    	if(list.peek() == null) {
    		return null;
    	}
        return list.peek().getThread();
    }

    /**
     * Executes the given number of instructions.
     * 
     * @see IScheduler#run(int)
     * @param numInstructions
     */
    public void run(int numInstructions)
    {
    	try {

    	
    	for(int i = 0; i < numInstructions; i++) {
    		sleepCheck()
    		tick();
    	}
    	}
    	
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void tick() {
    	VMThread thread = getCurrentThread();
    	VMThreadState state = thread.runInstruction();	
    	switch (state.getState()) {
    	case Running:
    		break;
    	case Yielded:
    		toEnd();
    		break;
    	case Complete:
    		if(list.peek().getPriority() < curPri) {
    			curPri = list.peek().getPriority();
    		}
    		toDead();
    		break;
    	case Sleeping:
    		toSleep(state.getTicksToSleep());
    		break;
    	}
}
}
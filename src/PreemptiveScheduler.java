import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import vmthread.VMThread;
import vmthread.VMThreadState;

public class PreemptiveScheduler implements IScheduler
{
	
	int jump;
	Queue<VMThread> list = new LinkedList<VMThread>();
	List<VMThread> dead = new LinkedList<VMThread>();
	List<SleepyThread> sleep = new LinkedList<SleepyThread>();
	int tick = 0;

    /**
     * Creates a CoopScheduler.
     * 
     * @param list the list of threads to be executed
     * @param slice clocks ticks allocated to each thread
     */
    public PreemptiveScheduler(List<VMThread> list, int slice)
    {
    	jump = slice;
    	this.list.addAll(list);
    }
    
	private void toDead() 
	{
		dead.add(getCurrentThread());
		list.remove(getCurrentThread());
		
	}
	
	private void toBack() {
		list.offer(list.poll());
	}
	
	private void tiquer() {


		if(tick == jump) {
			tick = 0;
			toBack();
		}
		
		
	}
	
	private void toSleep(int time) {
		sleep.add(new SleepyThread(time, list.poll()));
	}
	
	private void toWake(SleepyThread s) {
		list.offer(s.t);
		sleep.remove(s);
	}
	
	
	private void sleepCheck()
	{
		int i = 0;
		if(!sleep.isEmpty()) {
		for(SleepyThread s : sleep) {
			
			if(s.napTime == 0) {
				toWake(s);

			}
			else {
				s.napTime--;
			}
			i++;
			
		}
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

    		
    		tiquer();
    		
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
			tick++;
    		break;
    	case Yielded:
    		toBack();
    		tick = 0;
    		break;
    	case Complete:
    		toDead();
    		tick = 0;
    		break;
    	case Sleeping:
    		toSleep(state.getTicksToSleep());
    		tick = 0;
    		break;
    	}

    }
}

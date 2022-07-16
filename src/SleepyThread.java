import vmthread.VMThread;

public class SleepyThread {
	public int napTime;
	public VMThread t;
	public int p;
	
	public SleepyThread(int time, VMThread thread) {
		t = thread;
		napTime = time;
	}
	
	public SleepyThread(int time, VMThread thread, int pri) {
		this(time, thread);
		p = pri;
	}
	
	
}

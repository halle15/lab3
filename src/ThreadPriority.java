import vmthread.VMThread;

public class ThreadPriority implements Comparable<ThreadPriority>{
	
	public VMThread t;
	public int p;
		
	public ThreadPriority(VMThread t) {
		this.t = t;
		p = t.getPriority();
	}
	
	public int getPriority() {
		return p;
	}
	
	public VMThread getThread() {
		return this.t;
	}
	
	@Override
	public int compareTo(ThreadPriority t) {
		if(p > t.getPriority()) {
			return -1;
		}
		else if(p == t.getPriority()) {
			return 0;
		}
		else {
			return 1;
		}
	}
}

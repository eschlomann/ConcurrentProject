import java.util.concurrent.locks.ReentrantLock;

public class fullTest{

	//Initialize class variables
	// private int NUMTHREADS 	= 8;
	private static volatile int counter 	= 0;
	Thread[] threads;
	private CLH_Lock c_lock = new CLH_Lock();
	private MCS_Lock m_lock = new MCS_Lock();
	private ReentrantLock r_lock = new ReentrantLock();
	// T lock;

	//SubClass that is the "thread"
	//multiple of these are started, and are stored 
	//in the threads array
	class threadInstance extends Thread{
		private int threadID;
		private String type;
		private int countToThis;
		public threadInstance(int ID, String type, int countToThis){
			this.threadID 		= ID;
			this.type 			= type;
			this.countToThis 	= countToThis;
		}


		//Each thread attempts to count the counter 
		//when initiated.  Uses the tree lock to prevent
		//other threads from editting.
		public void run(){	
			for ( int i = 0; i < this.countToThis; i++ ){
				switch(this.type){
					case "CLH":
						c_lock.lock();
					break;	
					case "MCS":
						m_lock.lock();
					break;
					case "java":
						r_lock.lock();
					break;
				}
				try{
					counter = counter + 1;
				}
				finally{
					switch(this.type){
						case "CLH":
							c_lock.unlock();
						break;	
						case "MCS":
							m_lock.unlock();
						break;
						case "java":
							r_lock.unlock();
						break;
					}
				}
			}
		}
	}

	//function to run the test.  creates the threads, 
	//starts them, and joins them
	private void test(String type, int NUMTHREADS, int countToThis){
		threads = new Thread[NUMTHREADS];
		for (int i = 0; i < NUMTHREADS; i++){
			threads[i] = new threadInstance( i , type, countToThis);
			threads[i].start();
		}
		for (int i = 0; i < NUMTHREADS; i++){
			try{
				threads[i].join();
			}
			catch (InterruptedException e) {}
		}
	}

	//Tests the lock many times.  outputs the result at the end
	public static void main(String args[]){
		int numTries = Integer.parseInt(args[0]);
		int NUMTHREADS = Integer.parseInt(args[1]);
		int countToThis = Integer.parseInt(args[2]);
		String lockType = args[3];

		System.out.println(lockType);
		if (lockType.equals("CLH") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("CLH", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		} 
		if (lockType.equals("MCS") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("MCS", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		} 
		if (lockType.equals("java") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("java", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		}
	}
}
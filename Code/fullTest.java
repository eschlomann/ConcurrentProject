import java.util.concurrent.locks.ReentrantLock;

public class Test<T>{

	//Initialize class variables
	private int NUMTHREADS 	= 8;
	private int countToThis 	= 1000;
	private static volatile int counter 	= 0;
	private string lockType = "all";
	Thread[] threads;

	//SubClass that is the "thread"
	//multiple of these are started, and are stored 
	//in the threads array
	class threadInstance<T> extends Thread{
		private int threadID;
		private T lock;
		public threadInstance(int ID, string type){
			this.threadID = ID;
			switch (type) {
				case "CLH":
					lock = new CLH_Lock();
				break;
				case "MCS":

				break;
				case "java":
				break;
			}
		}


		//Each thread attempts to count the counter 
		//when initiated.  Uses the tree lock to prevent
		//other threads from editting.
		public void run(){	
			for ( int i = 0; i < countToThis; i++ ){
				lock.lock();
				try{
					counter = counter + 1;
				}
				finally{
					lock.unlock();
				}
			}
		}
	}

	//function to run the test.  creates the threads, 
	//starts them, and joins them
	private void test(string type){
		threads = new Thread[NUMTHREADS];
		for (int i = 0; i < NUMTHREADS; i++){
			threads[i] = new threadInstance( i , type );
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
		NUMTHREADS = Integer.parseInt(args[1]);
		countToThis = Integer.parseInt(args[2]);
		lockType = args[3];

		if (lockType == "CLH" || lockType == "all") {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				Test Tester = new Test();
				try{
					Tester.test("CLH");
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		} if (lockType == "MCS" || lockType == "all") {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				Test Tester = new Test();
				try{
					Tester.test("MCS");
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		} if (lockType == "java" || lockType == "all") {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				Test Tester = new Test();
				try{
					Tester.test("java");
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println((endTime - startTime)/1000000 + " milliseconds");
		}
	}
}
import java.util.concurrent.locks.ReentrantLock;

public class Test{

	//Initialize class variables
	final private static int NUMTHREADS 	= 8;
	final private static int countToThis 	= 500;
	private static volatile int counter 	= 0;
	Thread[] threads 	= new Thread[NUMTHREADS];
	// MCS_Lock lock 		= new MCS_Lock();
	CLH_Lock lock 		= new CLH_Lock();
	// ReentrantLock lock = new ReentrantLock();

	//SubClass that is the "thread"
	//multiple of these are started, and are stored 
	//in the threads array
	class threadInstance extends Thread{
		private int threadID;
		public threadInstance(int ID){
			this.threadID = ID;
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
	private void test(){
		for (int i = 0; i < NUMTHREADS; i++){
			threads[i] = new threadInstance( i );
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
		int wrong = 0;
		int numTries = 1;
		long startTime = System.nanoTime();
		 for(int i = 0; i < numTries; i++){
			Test Tester = new Test();
			try{
				Tester.test();
				if(counter == countToThis*NUMTHREADS){}
				else{
					wrong++;
				}
			}
			catch(Exception e) {}
			counter = 0;
		}
		long endTime = System.nanoTime();
		System.out.println((endTime - startTime)/1000000 + " milliseconds");
		System.out.println("Wrong: " + wrong + "/" + numTries);
	}
}
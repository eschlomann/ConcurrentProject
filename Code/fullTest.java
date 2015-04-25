import java.util.concurrent.locks.ReentrantLock;

public class fullTest{

	//Initialize class variables
	// private int NUMTHREADS 	= 8;
	private static volatile int counter;
	Thread[] threads;
	private CLH_Lock c_lock;
	private MCS_Lock m_lock;
	private ReentrantLock r_lock;
	private ReentrantLock r_lock_fair;
	private SZY_Lock s_lock;
	private Bakery b_lock;
	private Eis_Mcg e_lock;
	private static boolean contentionHigh;
	// T lock;

	//SubClass that is the "thread"
	//multiple of these are started, and are stored 
	//in the threads array
	class threadInstance extends Thread{
		private int threadID;
		private String type;
		private int countToThis;
		private int lowContCount;
		public threadInstance(int ID, String type, int countToThis){
			this.threadID 		= ID;
			this.type 			= type;
			this.countToThis 	= countToThis;
			this.lowContCount 	= countToThis;
			if ( contentionHigh == false ) {
				this.lowContCount = countToThis / countToThis;
			}
		}


		//Each thread attempts to count the counter 
		//when initiated.  Uses the tree lock to prevent
		//other threads from editting.
		public void run(){	
			// System.out.println(this.type);
			for ( int i = 0; i < this.lowContCount; i++ ){
				int m_counter = 0;
				if (contentionHigh == false) {
					for (int j = 0; j < this.countToThis; j++) {
						m_counter++;
					}
				}

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
					case "javaFair":
						r_lock_fair.lock();
					break;
					case "SZY":
						s_lock.lock(this.threadID);
					break;
					case "Bakery":
						b_lock.lock(this.threadID);
					break;
					case "EM":
						e_lock.lock(this.threadID);
					break;
				}
				try{
					if (contentionHigh == false ) {
						counter = counter + m_counter;
					} else {
						counter = counter + 1;
					}
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
						case "javaFair":
							r_lock_fair.unlock();
						break;
						case "SZY":
							s_lock.unlock(this.threadID);
						break;
						case "Bakery":
							b_lock.unlock(this.threadID);
						break;
						case "EM":
							e_lock.unlock(this.threadID);
						break;
					}
				}
			}
		}
	}

	//function to run the test.  creates the threads, 
	//starts them, and joins them
	private void test(String type, int NUMTHREADS, int countToThis){
		c_lock = new CLH_Lock();
		m_lock = new MCS_Lock();
		r_lock = new ReentrantLock();
		r_lock_fair = new ReentrantLock(true);
		s_lock = new SZY_Lock(NUMTHREADS);
		b_lock = new Bakery(NUMTHREADS);
		e_lock = new Eis_Mcg(NUMTHREADS);
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
		double numTries 	= (double)Integer.parseInt(args[0]);
		int NUMTHREADS 	= Integer.parseInt(args[1]);
		int countToThis = Integer.parseInt(args[2]);
		String lockType = args[3];
		String contention = args[4];

		if (contention.equals("high")) {
			contentionHigh = true;
		} else {
			contentionHigh = false;
		}

		if (lockType.equals("CLH") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("CLH", NUMTHREADS, countToThis);
				}
				catch(Exception e) {
					System.out.println("errored");
				}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println("CLH : "+((endTime - startTime)/numTries)/1000000 + " milliseconds avg");
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
			System.out.println("MCS : "+((double)(endTime - startTime)/numTries)/1000000 + " milliseconds avg");
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
			System.out.println("java : "+((endTime - startTime)/numTries)/1000000 + " milliseconds avg");
		}
		if (lockType.equals("javaFair") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("javaFair", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				System.out.println(counter);
				counter = 0;
			}
			long endTime = System.nanoTime(); 
			System.out.println("javaFair : "+((endTime - startTime)/numTries)/1000000 + " milliseconds avg");
		}
		if (lockType.equals("SZY") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("SZY", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println("SZY : "+((endTime - startTime)/numTries)/1000000 + " milliseconds");
        }
		if (lockType.equals("Bakery") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("Bakery", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}

				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println("Bakery : "+((endTime - startTime)/numTries)/1000000 + " milliseconds avg");
		}
		if (lockType.equals("EM") || lockType.equals("all")) {
			long startTime = System.nanoTime();
		 	for(int i = 0; i < numTries; i++){
				fullTest Tester = new fullTest();
				try{
					Tester.test("EM", NUMTHREADS, countToThis);
				}
				catch(Exception e) {}
				System.out.println(counter);
				counter = 0;
			}
			long endTime = System.nanoTime();
			System.out.println("Eis_Mcg : "+((endTime - startTime)/numTries)/1000000 + " milliseconds avg");
		}
	}
}

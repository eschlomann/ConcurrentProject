import java.util.concurrent.atomic.AtomicInteger;

public class SZY_Lock {
	final private AtomicInteger[] flagArray;
	final private int NUMTHREADS;
	public SZY_Lock(int numThreads){
		NUMTHREADS = numThreads;
		flagArray = new AtomicInteger[numThreads];
		for( int i = 0; i < NUMTHREADS; i++) {
			flagArray[i] = new AtomicInteger(0);
		}

	}
	public void lock(int me){
		flagArray[me] = new AtomicInteger(1); //Im interested
		//printFlagArray();
		while( testDoorOpen() == false ) {/*printFlagArray();*/}
		flagArray[me] = new AtomicInteger(3);
		//printFlagArray();
		if ( testWaiting() == true ) {
			flagArray[me] = new AtomicInteger(2);
			while ( testCS(me) == true ) { /*printFlagArray();*/}
		}
		flagArray[me] = new AtomicInteger(4);
		//printFlagArray();
		while ( checkLowerFinished(me) == false ) {/*printFlagArray();*/}
		//System.out.println("thread in CS");
	}

	public void unlock(int me) {
		while( checkWaitingRoom(me) == false ) {}
		flagArray[me] = new AtomicInteger(0);
	}

	private boolean testDoorOpen() {
		for ( int i = 0; i < NUMTHREADS; i++ ) {
			if( flagArray[i].get() == 3 || flagArray[i].get() == 4) {
				return false;
			}
		} return true;
	}
	
	private boolean testWaiting() {
		for ( int i = 0; i < NUMTHREADS; i++ ) {
			if( flagArray[i].get() == 1) {
				return true;
			}
		} return false;
	}

	private boolean testCS(int threadID) {
		for ( int i = 0; i < NUMTHREADS; i++ ) { //WHAT I CHANGED FROM THE PSEUDO CODE ON WIKIPEDIA
			if( flagArray[i].get() == 4) {
				return true;
			}
		} return false;
	}

	private boolean checkLowerFinished(int threadID) {
		for ( int i = 0; i < threadID-1; i++ ) {
			if( flagArray[i].get() == 2 || flagArray[i].get() == 3 || flagArray[i].get() == 4) {
				return false;
			}
		} return true;
	}

	private boolean checkWaitingRoom(int threadID) {
		for ( int i = threadID + 1; i < NUMTHREADS; i++ ) {
			if( flagArray[i].get() == 2 || flagArray[i].get() == 3) {
				return false;
			}
		} return true;		
	}

	private void printFlagArray() {
		for (int i = 0; i < NUMTHREADS; i++) {
			System.out.print(flagArray[i]);
			System.out.print(" ");
		} System.out.println();
	}
}
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bakery{
	//Need to use Atomic Boolean and Integers because volatile arrays aren't
	//a thing
	final private AtomicBoolean[] flag; 
	final private AtomicInteger[] label;
	private AtomicInteger maxLabel = new AtomicInteger(0);
	private static int numThreads;
	
	//initialize the class when called.
    //initialize all the atomic variables
	public Bakery (int numThreads){
		this.numThreads = numThreads;
		this.flag = new AtomicBoolean[numThreads];
		this.label = new AtomicInteger[numThreads];
		for (int i = 0; i < numThreads; i++){
			this.flag[i] = new AtomicBoolean(false);
			this.label[i] = new AtomicInteger(0);
		}
	}

	//The thread sets its flag to true, saying it wants to execute,
	//takes the next number available, and puts it in the labels
	//checks to see if it is the lowest number waiting, and waits.
	public void lock(int ID){
		this.flag[ID].set(true);
		for(AtomicInteger labels : this.label){
			if(labels.get() > maxLabel.get()){
				maxLabel = labels;
			}
		}
		this.label[ID].set(maxLabel.get() + 1);
		while(conflictsExist(ID)){};
	}

	//Checks to see if the given ID thread is the next lowerst number.
	//will return true otherwise
	private boolean conflictsExist(int ID){
    	for(int k = 0; k < this.numThreads; k++){
    		if(k == ID){
                continue;
    		}
    		if(this.flag[k].get() && (this.label[k].get() < this.label[ID].get()
    			 || (this.label[k].get() == this.label[ID].get() && k < ID))){
    			return true;
    		}
    	}
    	return false;
    }

	
    //Sets the threads flag to false, saying it does not want to execute.
	public void unlock(int ID){
		this.flag[ID].set(false);
	}
}
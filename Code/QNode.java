import java.util.concurrent.locks.ReentrantLock;

public class QNode {
	public QNode next;
	public boolean locked;
	public QNode(){
		next = null;
		locked = false;
	}


}
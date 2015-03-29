import java.util.concurrent.atomic.AtomicReference;

public class CLH_Lock{
	private ThreadLocal<QNode> myPred;  
    private ThreadLocal<QNode> myNode;  
    private AtomicReference<QNode> tail = new AtomicReference<QNode>(new QNode()); 

    public CLH_Lock(){
    	tail = new AtomicReference<QNode>(new QNode());
    	myNode = new ThreadLocal<QNode>() {
    		protected QNode initialValue() {
    			return new QNode();
    		}
    	};
    	myPred = new ThreadLocal<QNode>(){
    		protected QNode initialValue(){
    			return null;
    		}
    	};
    }

    public void lock(){
    	QNode qnode    = myNode.get();
    	qnode.locked   = true;
    	QNode pred     = tail.getAndSet(qnode);
    	myPred.set(pred);
    	while(pred.locked) {};
    }

	public void unlock(){
		QNode qnode   = myNode.get();
		qnode.locked  = false;
		myNode.set(myPred.get());
	}
}
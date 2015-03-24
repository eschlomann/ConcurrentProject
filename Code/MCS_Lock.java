import java.util.concurrent.atomic.AtomicReference;

public class MCS_Lock{
	AtomicReference<QNode> tail;
	ThreadLocal<QNode> myNode;

	public MCS_Lock(){
		AtomicReference queue = new AtomicReference<QNode>(null);
		myNode = new ThreadLocal<QNode>(){
			protected QNode initialValue(){
				return new QNode();
			}
		};
	}

	public void lock(){
		QNode qnode = myNode.get();
		QNode pred = tail.getAndSet(qnode);
		if(pred != null){
			qnode.locked = true;
			pred.next = qnode;
			while(qnode.locked){}
		}
	}

	public void unlock(){
		QNode qnode = myNode.get();
		if(qnode.next == null){
			if(tail.compareAndSet(qnode, null)){
				return;
			}
			while(qnode.next == null) {}
		}
		qnode.next.locked = false;
		qnode.next = null;
	}

}
import java.util.concurrent.atomic.AtomicReference;

public class MCS_Lock{
	private AtomicReference<QNode> tail = new AtomicReference<QNode>(null); 
	ThreadLocal<QNode> myNode;

	public MCS_Lock(){
		myNode = new ThreadLocal<QNode>(){
			protected QNode initialValue(){
				return new QNode();
			}
		};
	}

	public void lock(){
		QNode qnode 	= myNode.get();
		QNode pred 		= tail.getAndSet(qnode);
		if(pred != null){
			qnode.locked.set(true);
			pred.next 		= qnode;
			while(qnode.locked.get()){}
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
		qnode.next.locked.set(false);
		qnode.next 			= null;
	}

}
import java.util.concurrent.atomic.AtomicBoolean;

public class QNode {
	// public AtomicReference<QNode> next = new AtomicReference<QNode>(null); 
	public QNode 			next 	= null;
	public AtomicBoolean 	locked 	= new AtomicBoolean(false);
}
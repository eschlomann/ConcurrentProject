import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class QNode {
	public AtomicReference<QNode> next = new AtomicReference<QNode>(null); 
	// public QNode 			next 	= null;
	public AtomicBoolean 		locked = new AtomicBoolean(false);
}
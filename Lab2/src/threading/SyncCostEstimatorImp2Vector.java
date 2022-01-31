package threading;
import java.util.Vector;

public class SyncCostEstimatorImp2Vector implements SyncCostEstimator {
	
	private Vector<String> v;
	private int elements;
	
	public SyncCostEstimatorImp2Vector(int elements) {
		this.elements = elements;
		this.v = new Vector<String>(elements);
	}
	
	@Override
	public void run() {
		for (int i=0; i<elements; i++) {
			this.v.add("c");
		}
	}

	@Override
	public int getCounter() {
		return this.v.capacity();
	}

}

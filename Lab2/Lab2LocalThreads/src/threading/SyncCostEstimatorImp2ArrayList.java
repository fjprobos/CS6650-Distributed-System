package threading;
import java.util.ArrayList;
import java.util.List;

public class SyncCostEstimatorImp2ArrayList implements SyncCostEstimator {
	
	private List<String> v;
	private int elements;
	
	public SyncCostEstimatorImp2ArrayList(int elements) {
		this.elements = elements;
		this.v = new ArrayList<String>(elements);
	}
	
	@Override
	public void run() {
		for (int i=0; i<elements; i++) {
			this.v.add("c");
		}
	}

	@Override
	public int getCounter() {
		return this.v.size();
	}

}

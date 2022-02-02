package threading;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;


public class SyncCostEstimatorImp3HashMap implements SyncCostEstimator {
	
	private  Map<Integer, String> ht;
	private int elements;
	
	public SyncCostEstimatorImp3HashMap(int elements) {
		this.elements = elements;
		this.ht = Collections.synchronizedMap(new HashMap<Integer, String>());
	}
	
	@Override
	public void run() {
		for (int i=0; i<elements; i++) {
			this.ht.put(1, "c");
		}
	}

	@Override
	public int getCounter() {
		return this.ht.size();
	}

}

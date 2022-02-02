package threading;
import java.util.concurrent.ConcurrentHashMap;

public class SyncCostEstimatorImp3ConcurrentHashMap implements SyncCostEstimator {
	
	private  ConcurrentHashMap<Integer, String> ht;
	private int elements;
	
	public SyncCostEstimatorImp3ConcurrentHashMap(int elements) {
		this.elements = elements;
		this.ht = new ConcurrentHashMap<Integer, String>();
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

package threading;
import java.util.Hashtable;

public class SyncCostEstimatorImp3HashTable implements SyncCostEstimator {
	
	private  Hashtable<Integer, String> ht;
	private int elements;
	
	public SyncCostEstimatorImp3HashTable(int elements) {
		this.elements = elements;
		this.ht = new Hashtable<Integer, String>();
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

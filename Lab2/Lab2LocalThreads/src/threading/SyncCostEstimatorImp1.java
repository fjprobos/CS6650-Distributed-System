package threading;

public class SyncCostEstimatorImp1 implements SyncCostEstimator {
	
	private static int counter = 0;
    private static final Object lock = new Object();

    @Override
    public void run() {
            increaseCounter();
    }

    @Override
    public int getCounter() {
    	return counter;
    }
    
    private void increaseCounter() {
        synchronized (lock) {
        	for (int i=0; i<10; i++)
        	{
	            //System.out.println(Thread.currentThread().getName() + " : " + counter);
	            counter++;
        	}
        }
    }

}

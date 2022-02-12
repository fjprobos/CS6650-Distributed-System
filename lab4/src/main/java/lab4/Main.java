package lab4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class Main {

	public static StringBuilder sb;
	
	public static void main(String[] args) {
				
		try (PrintWriter writer = new PrintWriter("massiveUpload.csv")) {
			
		sb = new StringBuilder();
		sb.append("task,");
    sb.append("phase,");
    sb.append("numberOfRequestsPerThread,");
    sb.append("threadId,");
    sb.append("requestId,");
    sb.append("start,");
    sb.append("end,");
    sb.append("duration");
    sb.append('\n');
    
    for (int l=32; l<33; l*=2) {
			run(l, l, "Post Latency Analysis", "Phase 1");
    }
	
    writer.write(sb.toString());
		} 
		catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
	}
	
	public static void run(int threads, int requests, String taskName, String phase) {
		
		Timestamp start = new Timestamp(System.currentTimeMillis());
		int threadsPerRequest = requests/threads;
    CountDownLatch countDownLatch = new CountDownLatch(threads);
    List<SkierClient> clients = new ArrayList<SkierClient>();
  	List<Long> durations =  new ArrayList<Long>();
  	List<Timestamp> starts = new ArrayList<Timestamp>();
  	List<Timestamp> ends = new ArrayList<Timestamp>();
		
		for (int i=0; i<threads; i++) {
			SkierClient client = new SkierClient(threadsPerRequest, countDownLatch);
			clients.add(client);
			Thread thread = new Thread(client);
		    thread.start();
		}
		
		// Wait for the thread pool to finish
		try {
			countDownLatch.await();
			// Fetch the monitoring data collected
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Get the final timestamp and calculate walTime
		Timestamp end = new Timestamp(System.currentTimeMillis());
		long wallTime = end.getTime() - start.getTime();
		
		// Organize info in lists and csv
		for(SkierClient c : clients) {
			for(int i=0; i<c.getDurations().size(); i++) {
				sb.append(taskName);
		    sb.append(",");
		    sb.append(phase);
		    sb.append(',');
		    sb.append(threadsPerRequest);
		    sb.append(',');
		    sb.append(c.toString());
		    sb.append(',');
		    sb.append(i);
		    sb.append(',');
		    sb.append(c.getStarts().get(i));
		    sb.append(',');
		    sb.append(c.getEnds().get(i));
		    sb.append(',');
		    sb.append(c.getDurations().get(i));
		    sb.append('\n');
			}
			durations.addAll(c.getDurations());
			starts.addAll(c.getStarts());
			ends.addAll(c.getEnds());
		}
	
		
		System.out.println("Task: " + taskName);
		System.out.println("Phase): " + phase);
		System.out.println("Total requests: " + requests);
		System.out.println("Total threads: " + threads);
		System.out.println("Wall Time: " + wallTime);
		calculateStatistics(durations, start, end);
		System.out.println("\n");
		
	}

	public static void runlatencyTest(int requestNumber) {
		
		try (PrintWriter writer = new PrintWriter("latencyTestOutput.csv")) {
			  
		  sb = new StringBuilder();
		  sb.append("thread_id,");
		  sb.append("start,");
		  sb.append("end,");
		  sb.append("duration");
		  sb.append('\n');
		  
			Thread thread = new Thread(new SkierClient(requestNumber, null));
	    thread.start();
		  
		  writer.write(sb.toString());
			  
		}
		catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
	}
	
	public static void calculateStatistics(List<Long> durations, Timestamp start, Timestamp finish) {
		Collections.sort(durations);
		long min = durations.get(0);
		long max = durations.get(durations.size() - 1);
		Double averageDuration =  durations.stream().collect(Collectors.averagingDouble(d -> d));
		int medianIndex = durations.size()/2 - 1;
		int percentile99Index = (int) ((int) durations.size()*0.99) - 1;
		long median = durations.get(medianIndex);
		long percentile99 = durations.get(percentile99Index);
		long totalDuration = (finish.getTime() - start.getTime());
		int totalRequests = durations.size();
		double throughput = totalRequests/(totalDuration/1000.0);
		
		System.out.println("Latency statistics:");
		System.out.println("Average Duration [ms]: " + averageDuration);
		System.out.println("Median Duration [ms]: " + median);
		System.out.println("Throughput [r/s]: " + throughput);
		System.out.println("Percentile 99th Duration [ms]: " + percentile99);
		System.out.println("Min Duration [ms]: " + min);
		System.out.println("Max Duration [ms]: " + max);
		System.out.println("\n");
		
		
	}
}

package lab4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class Main {

	public static StringBuilder sb;
	
	public static void main(String[] args) {
				
		try (PrintWriter writer = new PrintWriter("multiVsSingle.csv")) {
			
		sb = new StringBuilder();
		sb.append("task,");
    sb.append("type,");
    sb.append("numberOfRequests,");
    sb.append("numberOfThreadss,");
    sb.append("durationMiliseconds");
    sb.append('\n');
    
    for (int l=32; l<257; l*=2) {
			run1m(l);
			run1s(l);
    }
	
    writer.write(sb.toString());
		} 
		catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
		
		//runlatencyTest(10000);
	}
	
	public static void run1m(int limit) {
		Timestamp start = new Timestamp(System.currentTimeMillis());
    CountDownLatch countDownLatch = new CountDownLatch(limit);
		
		for (int i=0; i<limit; i++) {
			Thread thread = new Thread(new SkierClient(1, countDownLatch));
		    thread.start();
		}
		
		// Wait for the thread pool to finish
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		long diff = end.getTime() - start.getTime();
		
		sb.append("Get Vertical,");
    sb.append("Multi thread,");
    sb.append(limit);
    sb.append(',');
    sb.append(limit);
    sb.append(',');
    sb.append(diff);
    sb.append('\n');
		
		System.out.println("Multithreaded Get Method Vertical ");
		System.out.println("Number of threads(one request each): " + limit);
		System.out.println("Duration [ms]: " + diff);
		System.out.println("\n");
		
	}
	
	public static void run1s(int limit) {
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		// This time, we use join inside the loop to wait for the threads to finish
		
		for (int i=0; i<1; i++) {
			Thread thread = new Thread(new SkierClient(limit, null));
	    thread.start();
	    try {
	    	thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		long diff = end.getTime() - start.getTime();
		
		sb.append("Get Vertical,");
    sb.append("Single thread,");
    sb.append(limit);
    sb.append(',');
    sb.append(1);
    sb.append(',');
    sb.append(diff);
    sb.append('\n');
		
    System.out.println("Single Thread Get Method Vertical ");
		System.out.println("Number of requests: " + limit);
		System.out.println("Duration [ms]: " + diff);
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
	
	public void calculateStatistics(List<Long> durations, Timestamp start, Timestamp finish) {
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
		long throughput = totalRequests/(totalDuration/1000);
		
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

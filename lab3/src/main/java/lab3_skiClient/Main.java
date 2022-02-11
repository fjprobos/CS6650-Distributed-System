package lab3_skiClient;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class Main {

	public static StringBuilder sb;
	
	public static void main(String[] args) {
		
		boolean latencyTest = false;
		
		if (latencyTest) {
			runlatencyTest(200);
		}
		else {			
			try (PrintWriter writer = new PrintWriter("multiVsSingle.csv")) {
				
			sb = new StringBuilder();
			sb.append("task,");
	    sb.append("type,");
	    sb.append("numberOfRequests,");
	    sb.append("numberOfThreadss,");
	    sb.append("durationMiliseconds");
	    sb.append('\n');
	    
	    for (int l=1; l<10000; l*=10) {
			run1m(l);
			run1s(l);
	    }
		
	    writer.write(sb.toString());
			} 
			catch (FileNotFoundException e) {
			      System.out.println(e.getMessage());
			}
			
			runlatencyTest(10000);
		}
	}
	
	public static void run1m(int limit) {
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		for (int i=0; i<limit; i++) {
			Thread thread = new Thread(new SkierClient());
		    thread.start();
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
		
		for (int i=0; i<limit; i++) {
			Thread thread = new Thread(new SkierClient());
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
		SkierClient sc = new SkierClient();
    sc.runLatencyTest(requestNumber);
	}
}

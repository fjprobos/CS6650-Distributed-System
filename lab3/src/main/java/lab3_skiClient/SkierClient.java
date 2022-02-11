package lab3_skiClient;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SkierClient implements Runnable {
	  
	private static String url = "http://18.206.124.170:8080/lab2_TomcatServlets_war/skiers/12/vertical";
	
	public void run() {
	  // Create an instance of HttpClient.
	  HttpClient client = new HttpClient();
		
	  // Create a method instance.
	  GetMethod method = new GetMethod(url);
		
	  // Provide custom retry handler is necessary
	  method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		
	  try {
	    // Execute the method.
	    int statusCode = client.executeMethod(method);
		
	    if (statusCode != HttpStatus.SC_OK) {
		    System.err.println("Method failed: " + method.getStatusLine());
		}
		
		// Read the response body.
	    InputStream stream = method.getResponseBodyAsStream();
	    byte[] responseBody = stream.readAllBytes();
		//byte[] responseBody = method.getResponseBodyAsStream();
		
		// Deal with the response.
		// Use caution: ensure correct character encoding and is not binary data
		// System.out.println(new String(responseBody));
		
		} catch (HttpException e) {
		  System.err.println("Fatal protocol violation: " + e.getMessage());
		  e.printStackTrace();
		} catch (IOException e) {
		  System.err.println("Fatal transport error: " + e.getMessage());
		  e.printStackTrace();
		} finally {
		  // Release the connection.
		      method.releaseConnection();
		    }  
	  }
	
	public void runLatencyTest(int requests) {
		
		StringBuilder sb;
		List<Long> durations = new ArrayList<Long>();
		Timestamp generalStart = new Timestamp(System.currentTimeMillis());
		
		try (PrintWriter writer = new PrintWriter("latencyTestOutput.csv")) {
		  
		  sb = new StringBuilder();
		  sb.append("thread_id,");
		  sb.append("start,");
		  sb.append("end,");
		  sb.append("duration");
		  sb.append('\n');
		    
		  // Create an instance of HttpClient.
		  HttpClient client = new HttpClient();
			
		  // Create a method instance.
		  GetMethod method = new GetMethod(url);
			
		  // Provide custom retry handler is necessary
		  method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));
		  try {
			  
			for (int i = 0; i<requests; i++) {
				
				Timestamp start = new Timestamp(System.currentTimeMillis());
				
				// Execute the method.
		    int statusCode = client.executeMethod(method);
				
			  if (statusCode != HttpStatus.SC_OK) {
				    System.err.println("Method failed: " + method.getStatusLine());
				}
				
				// Read the response body.
			  InputStream stream = method.getResponseBodyAsStream();
			  byte[] responseBody = stream.readAllBytes();
				//byte[] responseBody = method.getResponseBodyAsStream();
				
				// Deal with the response.
				// Use caution: ensure correct character encoding and is not binary data
				// System.out.println(new String(responseBody));
			    
			  Timestamp end = new Timestamp(System.currentTimeMillis());
				long diff = end.getTime() - start.getTime();
				durations.add(diff);
			    
		    sb.append(i);
		    sb.append(',');
		    sb.append(start);
		    sb.append(',');
		    sb.append(end);
		    sb.append(',');
		    sb.append(diff);
		    sb.append('\n');
				
			}
			} catch (HttpException e) {
			  System.err.println("Fatal protocol violation: " + e.getMessage());
			  e.printStackTrace();
			} catch (IOException e) {
			  System.err.println("Fatal transport error: " + e.getMessage());
			  e.printStackTrace();
			} finally {
			  // Release the connection.
			      method.releaseConnection();
			      writer.write(sb.toString());
			      Timestamp generalEnd = new Timestamp(System.currentTimeMillis());
			      calculateStatistics(durations, generalStart, generalEnd);
			    }
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

package lab4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class SkierClient implements Runnable {
	  
	private static String url = "http://localhost:8080/lab2_TomcatServlets_war_exploded/skiers/12/vertical";
	private int requestsNumber;
	private List<Long> durations;
	private List<Timestamp> starts;
	private List<Timestamp> ends;
	private CountDownLatch countDownLatch;
	private int skierRangeMin;
	private int skierRangeMax;
	private int lifts;
	private int startTime;
	private int periodDuration;
	
	public SkierClient (int requests, CountDownLatch countDownLatch, int skierRangeMin,
			int skierRangeMax, int startTime, int endTime, int lifts) {
		this.durations = new ArrayList<Long>();
		this.starts = new ArrayList<Timestamp>();
		this.ends = new ArrayList<Timestamp>();
		this.requestsNumber = requests;
		this.countDownLatch = countDownLatch;
		this.skierRangeMin = skierRangeMin;
		this.skierRangeMax = skierRangeMax;
		this.lifts = lifts;
		this.startTime = startTime;
		this.periodDuration = endTime - startTime;
		
	}
	
	public List<Long> getDurations() {
		return this.durations;
	}
	
	public List<Timestamp> getStarts() {
		return this.starts;
	}
	
	public List<Timestamp> getEnds() {
		return this.ends;
	}
	
	public Timestamp getFinalTimeStamp() {
		if (this.ends.isEmpty()) {
			return null;
		}
		else {
			return this.ends.get(this.ends.size() - 1);
		}
	}
	
	public Timestamp getFirstTimeStamp() {
		if (this.starts.isEmpty()) {
			return null;
		}
		else {
			return this.ends.get(0);
		}
	}
	
	public void run() {
		
  	// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
			
	  // Create a method instance.
    PostMethod postMethod = new PostMethod(url);
		
	  try {
   
		  // Provide custom retry handler is necessary
	    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(5, false));
	  	
			for (int i = 0; i<this.requestsNumber; i++) {
				
				// Post body creation
				Random ran = new Random();
        Integer randomSkierID = ran.nextInt(this.skierRangeMax - this.skierRangeMin) + this.skierRangeMin;
        Integer randomLiftID = ran.nextInt(this.lifts);
        StringRequestEntity requestEntity = new StringRequestEntity(
  	    		"{'time': "+ this.startTime + ran.nextInt(this.periodDuration) 
  	    		+", 'liftID': "+randomLiftID+", 'waitTime': 3}",
  	        "application/json",
  	        "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        
        // Latency recording start
				Timestamp start = new Timestamp(System.currentTimeMillis());
				this.starts.add(start);
				// Execute the method.
		    int statusCode = client.executeMethod(postMethod);
				
			  if (statusCode != HttpStatus.SC_OK) {
				    System.err.println("Method failed: " + postMethod.getStatusLine());
				}
				
				// Read the response body.
			  InputStream stream = postMethod.getResponseBodyAsStream();
			  byte[] responseBody = stream.readAllBytes();
				//byte[] responseBody = method.getResponseBodyAsStream();
				
				// Deal with the response.
				// Use caution: ensure correct character encoding and is not binary data
				System.out.println(new String(responseBody));
			    
			  Timestamp end = new Timestamp(System.currentTimeMillis());
			  this.ends.add(end);
				long diff = end.getTime() - start.getTime();
				this.durations.add(diff);	
			}
		
		} 
	  catch (IOException e) {
		  System.err.println("Fatal transport error: " + e.getMessage());
		  e.printStackTrace();
		} 
	  finally {
		  // Release the connection.
		      postMethod.releaseConnection();
		      if (this.countDownLatch != null) {
		      	//System.err.println("Counted Down");
		      	this.countDownLatch.countDown();
		      }
			}
  }
	
}
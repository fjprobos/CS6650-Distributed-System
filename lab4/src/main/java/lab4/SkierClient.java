package lab4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpStatus;

public class SkierClient implements Runnable {
	  
	private static String url = "http://18.206.124.170:8080/lab2_TomcatServlets_war/skiers/12/vertical";
	private int requestsNumber;
	private List<Long> durations;
	private List<Timestamp> starts;
	private List<Timestamp> ends;
	
	public SkierClient (int requests) {
		this.durations = new ArrayList<Long>();
		this.starts = new ArrayList<Timestamp>();
		this.ends = new ArrayList<Timestamp>();
		this.requestsNumber = requests;
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
	  GetMethod method = new GetMethod(url);
		
	  // Provide custom retry handler is necessary
	  method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(5, false));
	  try {
			for (int i = 0; i<this.requestsNumber; i++) {
				Timestamp start = new Timestamp(System.currentTimeMillis());
				this.starts.add(start);
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
		      method.releaseConnection();
			}
  }
	
}
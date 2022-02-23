package lab4;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.net.URI;
import java.net.http.HttpClient;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SkierClient implements Runnable {
	  
	private static String url = "http://54.90.199.164:8080/lab2_TomcatServlets_war/skiers/12/seasons/1/days/12/skiers/3";
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
	
	@Override
	public void run() {
		
		for (int i = 0; i<this.requestsNumber; i++) {
			
			// Post body creation
			Random ran = new Random();
      Integer randomLiftID = ran.nextInt(this.lifts);
      Integer time = (this.startTime + ran.nextInt(this.periodDuration));
		
			HashMap<String, String> values = new HashMap<String, String>() {{
	      put("time", time.toString());
	      put ("liftID", randomLiftID.toString());
	      put ("waitTime", "3");
			}};
	
		  ObjectMapper objectMapper = new ObjectMapper();
		  String requestBody = "";  
			try {
				requestBody = objectMapper
				        .writeValueAsString(values);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		
		  HttpClient client = HttpClient.newHttpClient();
		  HttpRequest request = HttpRequest.newBuilder()
		          .uri(URI.create(url))
		          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
		          .build();
		
		  // Latency recording start
			Timestamp start = new Timestamp(System.currentTimeMillis());
			this.starts.add(start);
		  try {
				HttpResponse<String> response = client.send(request,
				        HttpResponse.BodyHandlers.ofString());
				this.countDownLatch.countDown();
				//System.err.println(response.body());
				Timestamp end = new Timestamp(System.currentTimeMillis());
			  this.ends.add(end);
				long diff = end.getTime() - start.getTime();
				this.durations.add(diff);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
  }
	
}
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
		
		if (args.length < 5) {
      return;
		}

	  Integer c = 0;
	  Integer numThreads = 0;
	  Integer numRuns = 0;
	  Integer numSkiers = 0;
	  Integer numLifts = 0;
	  for (String s : args) {
	      if (c == 0) {
	          numThreads = Integer.parseInt(s);
	      }
	      else if (c == 1) {
	          numSkiers = Integer.parseInt(s);
	      }
	      else if (c == 2) {
	          numLifts = Integer.parseInt(s);
	      }
	      else if (c == 3) {
	          numRuns = Integer.parseInt(s);
	      }
	      else if (c == 4) {
	          Integer portAddress = Integer.parseInt(s);
	      }
	      c += 1;
	  }
	  
	  Integer launch = numThreads / 4;
    Integer skier_num = numSkiers;
    Integer lifts = numLifts;
    Integer successes = 0;
    Integer no_successes = 0;
    Integer requests = 0;
    Integer posts_to_send_1 = (int) (numRuns * 0.2 * (skier_num / launch));
    Integer posts_to_send_2 = (int) (numRuns * 0.6 * (skier_num / launch));
    Integer posts_to_send_3 = (int) (numRuns * 0.1);
	  	
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
    
    for (int i = 0; i < launch; i++) {
        if (i > 0) {
            Integer start_time = 1;
            Integer end_time = 90;
            Integer skier_range_min = (numThreads * i) + 1;
            Integer skier_range_max = numThreads * (1 + i);
            run(launch, posts_to_send_1*launch, "Post Latency Analysis", "Phase 1", 
            		skier_range_min, skier_range_max, start_time, end_time, lifts);
        }
        else {
          Integer start_time = 1;
          Integer end_time = 90;
          Integer starting_skier_min = 1;
          Integer starting_skier_max = numThreads;
          run(launch, posts_to_send_1*launch, "Post Latency Analysis", "Phase 1", 
          		starting_skier_min, starting_skier_max, start_time, end_time, lifts);
        }
        if (i >= 0.20 * launch) {
          for (int j = 0; j < numThreads; j++) {
            if (j > 0) {
                Integer start_time = 91;
                Integer end_time = 360;
                Integer skier_range_min = (numSkiers/numThreads * i) + 1;
                Integer skier_range_max = numSkiers/numThreads * (1 + i);
                run(launch, posts_to_send_2*launch, "Post Latency Analysis", "Phase 2", 
                		skier_range_min, skier_range_max, start_time, end_time, lifts);
            }
            else {
              Integer start_time = 91;
              Integer end_time = 360;
              Integer starting_skier_min = 1;
              Integer starting_skier_max = numSkiers/numThreads;
              run(launch, posts_to_send_2*launch, "Post Latency Analysis", "Phase 2", 
              		starting_skier_min, starting_skier_max, start_time, end_time, lifts);
            }
            if (j >= 0.20 * numThreads) {
              for (int k = 0; k < 0.10 * numThreads; k++) {
                if (k > 0) {
                    Integer start_time = 361;
                    Integer end_time = 420;
                    Integer skier_range_min = (numThreads * i) + 1;
                    Integer skier_range_max = numThreads * (1 + i);
                    run(launch, posts_to_send_3*launch, "Post Latency Analysis", "Phase 3", 
                    		skier_range_min, skier_range_max, start_time, end_time, lifts);
                }
                else {
                	Integer start_time = 361;
                  Integer end_time = 420;
                  Integer starting_skier_min = 1;
                  Integer starting_skier_max = numThreads;
                  run(launch, posts_to_send_3*launch, "Post Latency Analysis", "Phase 3", 
                  		starting_skier_min, starting_skier_max, start_time, end_time, lifts);
                }
              }       
            }
          }
        }
			}
    	writer.write(sb.toString());
		}
		catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
	}
	
	public static void run(int threads, int requests, String taskName, String phase,
			int skierMin, int skierMax, int timeMin, int timeMax, int lifts) {
		
		Timestamp start = new Timestamp(System.currentTimeMillis());
		int threadsPerRequest = requests/threads;
    CountDownLatch countDownLatch = new CountDownLatch(threads);
    List<SkierClient> clients = new ArrayList<SkierClient>();
  	List<Long> durations =  new ArrayList<Long>();
  	List<Timestamp> starts = new ArrayList<Timestamp>();
  	List<Timestamp> ends = new ArrayList<Timestamp>();
		
		for (int i=0; i<threads; i++) {
			SkierClient client = new SkierClient(threadsPerRequest, countDownLatch, skierMin,
					skierMax, timeMin, timeMax, lifts);
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
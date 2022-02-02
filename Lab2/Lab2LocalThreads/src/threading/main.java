package threading;

import java.sql.Timestamp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class main {
	
	public static StringBuilder sb;

	public static void main(String[] args) {
		
		try (PrintWriter writer = new PrintWriter("test.csv")) {
			
		sb = new StringBuilder();
	    sb.append("task,");
	    sb.append("type,");
	    sb.append("limit");
	    sb.append("durationMiliseconds");
	    sb.append('\n');
	    
	    for (int l=10; l<10000000; l*=10) {
			run1(l);
			run2(l);
			run3a(l);
			run3b(l, 100);
	    }
		
		writer.write(sb.toString());
		} 
		catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
		
	}
	
	public static void run1(int limit) {
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		for (int i=0; i<limit; i++) {
			Thread thread = new Thread(new SyncCostEstimatorImp1());
		    thread.start();
		    try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		long diff = end.getTime() - start.getTime();
		
		sb.append("Counter,");
	    sb.append("Multi thread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diff);
	    sb.append('\n');
		
		System.out.println("Counter Estimator: " + limit);
		System.out.println("Counter Duration: " + diff);
		
	}
	
	public static void run2(int limit) {
		Timestamp startVector = new Timestamp(System.currentTimeMillis());
		Thread thread = new Thread(new SyncCostEstimatorImp2Vector(limit));
	    thread.start();
	    try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    Timestamp endVector = new Timestamp(System.currentTimeMillis());
		long diffVector = endVector.getTime() - startVector.getTime();
		
		Timestamp startArray = new Timestamp(System.currentTimeMillis());
		Thread thread2 = new Thread(new SyncCostEstimatorImp2ArrayList(limit));
	    thread2.start();
	    try {
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    Timestamp endArray = new Timestamp(System.currentTimeMillis());
		long diffArray = endArray.getTime() - startArray.getTime();
		
		sb.append("Collections1,");
	    sb.append("VectorSingleThread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffVector);
	    sb.append('\n');
	    sb.append("Collections1,");
	    sb.append("ArrayListSingleThread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffArray);
	    sb.append('\n');
		
		System.out.println("Vector/Arraylist Estimator: " + limit);
		System.out.println("Vector Duration: " + diffVector);
		System.out.println("ArrayList Duration: " + diffArray);
		System.out.println("\n");
	}
	
	public static void run3a(int limit) {
		//Hash Table
		Timestamp startHt = new Timestamp(System.currentTimeMillis());
		Thread thread = new Thread(new SyncCostEstimatorImp3HashTable(limit));
	    thread.start();
	    try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    Timestamp endHt = new Timestamp(System.currentTimeMillis());
		long diffHt = endHt.getTime() - startHt.getTime();
		
		//Hash Map		
		Timestamp startHm = new Timestamp(System.currentTimeMillis());
		Thread thread2 = new Thread(new SyncCostEstimatorImp3HashMap(limit));
	    thread2.start();
	    try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    Timestamp endHm = new Timestamp(System.currentTimeMillis());
		long diffHm = endHm.getTime() - startHm.getTime();
		
		//Concurrent Hash Map		
		Timestamp startChm = new Timestamp(System.currentTimeMillis());
		Thread thread3 = new Thread(new SyncCostEstimatorImp3ConcurrentHashMap(limit));
	    thread3.start();
	    try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    Timestamp endChm = new Timestamp(System.currentTimeMillis());
		long diffChm = endChm.getTime() - startChm.getTime();
		
		sb.append("Collections2,");
	    sb.append("HashTableSingleThread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffHt);
	    sb.append('\n');
	    sb.append("Collections2,");
	    sb.append("HashMapSingleThread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffHm);
	    sb.append('\n');
	    sb.append("Collections2,");
	    sb.append("ConcurrentHashMapSingleThread,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffChm);
	    sb.append('\n');
		
		System.out.println("HashTable/HashMap Estimator: " + limit);
		System.out.println("HashTable Duration: " + diffHt);
		System.out.println("HashMap Duration: " + diffHm);
		System.out.println("ConcurrentHashMap Duration: " + diffChm);
		System.out.println("\n");
	}
	
	public static void run3b(int limit, int threads) {
		// Hash Table
		Timestamp startHt = new Timestamp(System.currentTimeMillis());
		for (int i=0; i<threads; i++) {
			Thread thread = new Thread(new SyncCostEstimatorImp3HashTable(limit/threads));
		    thread.start();
		    try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Timestamp endHt = new Timestamp(System.currentTimeMillis());
		long diffHt = endHt.getTime() - startHt.getTime();
		
		// Hash Map
		Timestamp startHm = new Timestamp(System.currentTimeMillis());
		for (int i=0; i<threads; i++) {
			Thread thread = new Thread(new SyncCostEstimatorImp3HashMap(limit/threads));
		    thread.start();
		    try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Timestamp endHm = new Timestamp(System.currentTimeMillis());
		long diffHm = endHm.getTime() - startHm.getTime();
				
		// Concurrent Hash Map
		Timestamp startChm = new Timestamp(System.currentTimeMillis());
		for (int i=0; i<threads; i++) {
			Thread thread = new Thread(new SyncCostEstimatorImp3ConcurrentHashMap(limit/threads));
		    thread.start();
		    try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Timestamp endChm = new Timestamp(System.currentTimeMillis());
		long diffChm = endChm.getTime() - startChm.getTime();
		
		sb.append("Collections2,");
	    sb.append("HashTableMultipleThreads,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffHt);
	    sb.append('\n');
	    sb.append("Collections2,");
	    sb.append("HashMapMultipleThreads,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffHm);
	    sb.append('\n');
	    sb.append("Collections2,");
	    sb.append("ConcurrentHashMapMultipleThreads,");
	    sb.append(limit);
	    sb.append(',');
	    sb.append(diffChm);
	    sb.append('\n');
		
		System.out.println("Multithreaded HashTable/HashMap Estimator: " + limit);
		System.out.println("HashTable Duration: " + diffHt);
		System.out.println("HashMap Duration: " + diffHm);
		System.out.println("ConcurrentHashMap Duration: " + diffChm);
		System.out.println("\n");
	}
}

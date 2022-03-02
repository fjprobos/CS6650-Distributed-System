package lab5;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

	public static void main(String[] args) {
		
		//Send s = new Send();
		//s.sendMessage("prueba 2");
		
		// RPC Server startup -> we use a separate thread
		RPCServer server = new RPCServer();
		Thread thread = new Thread(server);
    thread.start();
		
		
		// RPC Client call
    try (RPCClient fibonacciRpc = new RPCClient()) {
      for (int i = 0; i < 32; i++) {
          String i_str = Integer.toString(i);
          System.out.println(" [x] Requesting fib(" + i_str + ")");
          String response = fibonacciRpc.call(i_str);
          System.out.println(" [.] Got '" + response + "'");
      }
    } 
    catch (IOException | TimeoutException | InterruptedException e) {
      e.printStackTrace();
    }
		

	}

}

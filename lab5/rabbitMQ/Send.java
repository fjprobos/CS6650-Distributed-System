package lab5;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import java.nio.charset.StandardCharsets;
import com.rabbitmq.client.Channel;


public class Send {
	
	private final static String QUEUE_NAME = "hello";
	private ConnectionFactory factory;
	
	public Send() {
		this.factory = new ConnectionFactory();
		this.factory.setHost("localhost");
		
	}
	
	public void sendMessage(String message) {
    try (Connection connection = this.factory.newConnection();
        Channel channel = connection.createChannel()) {
       channel.queueDeclare(QUEUE_NAME, false, false, false, null);
       channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
       System.out.println(" [x] Sent '" + message + "'");
   }
    catch (Exception e) {
			e.printStackTrace();
		}
	}
}

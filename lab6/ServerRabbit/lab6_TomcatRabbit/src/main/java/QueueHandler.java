import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.Connection;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;


public class QueueHandler {

    private final String QUEUE_NAME;
    private final String host;
    public ConnectionFactory factory;

    public QueueHandler(String host, String queueName) {
        this.QUEUE_NAME = queueName;
        this.host = host;
        this.factory = new ConnectionFactory();
        try {
            String uri = "amqp://test:test@"+this.host+":5672";
            this.factory.setUri(uri);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
            var a = channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consumeMessage() {
        try (Connection connection = this.factory.newConnection();
             Channel channel = connection.createChannel()) {
//       var a = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//       var tag = channel.basicConsume(QUEUE_NAME, true, null);
//       var message = tag.getBytes(StandardCharsets.UTF_8);
            boolean autoAck = false;
            GetResponse response = channel.basicGet(QUEUE_NAME, autoAck);
            if (response == null) {
                // No message retrieved.
            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                String s2 = new String(body, "UTF-8");
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                System.out.println(s2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
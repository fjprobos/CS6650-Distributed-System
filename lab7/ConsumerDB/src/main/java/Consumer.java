import com.rabbitmq.client.*;
import entity.LiftRide;
import entity.Season;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;

public class Consumer {

    private static Map<String, Season> map = new ConcurrentHashMap<>();
    private static int parseInt(String s) {
        return Integer.parseInt(s);
    }
    private static void retrieveMessageFromServer() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("54.91.202.62");
        factory.setUsername("test");
        factory.setPassword("test");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("client_generated_1", true, false, false, null);
            channel.basicConsume("client_generated_1", true, (consumerTag, m) -> {
                final String[] message = new String(m.getBody(), "UTF-8").split("/");
                Season season = new Season(parseInt(message[1]), parseInt(message[2]), parseInt(message[3]));
                map.put(message[0], season);
                System.out.println("receive messages");
            }, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveLiftRideFromServer() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("52.71.70.224");
        factory.setUsername("test");
        factory.setPassword("test");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("client_generated_1", true, false, false, null);
            channel.basicConsume("client_generated_1", true, (consumerTag, m) -> {
                final String[] message = new String(m.getBody(), "UTF-8").split(",");
                int resortID = Integer.parseInt(StringUtils.substringBetween(message[0], ":"));
                int seasonID = Integer.parseInt(StringUtils.substringBetween(message[1], ":"));
                int day = Integer.parseInt(StringUtils.substringBetween(message[2], ":"));
                int waitTime = Integer.parseInt(StringUtils.substringBetween(message[4], ":"));
                int liftID = Integer.parseInt(StringUtils.substringBetween(message[5], ":"));


                System.out.println(m.getBody()[1]);
                LiftRide liftRide = new LiftRide(parseInt(message[0]),
                        resortID,
                        seasonID,
                        day,
                        waitTime,
                        liftID);
                LiftRideDao liftRideDao = new LiftRideDao();
                liftRideDao.createLiftRide(liftRide);
                System.out.println("stored data to database!");
            }, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        int numThreads = 1;
        if (args.length != 0) {
            numThreads = Integer.parseInt(args[0]);
        }
        for (int i = 0; i < numThreads; i++) {
            Runnable thread = () -> {
                retrieveLiftRideFromServer();
                //retrieveMessageFromServer();
            };
            new Thread(thread).start();
        }
    }
}

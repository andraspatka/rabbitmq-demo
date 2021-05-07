package andras.patka.rabbitmq.demo.control;

import andras.patka.rabbitmq.demo.model.MessageType;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class RabbitMqService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqService.class);

    @Inject
    RabbitMQClient rabbitMQClient;

    private Channel channel;

    private final String EXCHANGE_NAME = "exchange";

    public void onApplicationStart(@Observes StartupEvent event) {
        // on application start prepare the queues and message listener
        setupQueues();
    }

    private void setupQueues() {
        try {
            // create a connection
            Connection connection = rabbitMQClient.connect();
            // create a channel
            channel = connection.createChannel();
            String primaryName = MessageType.PRIMARY.toString();
            String secondaryName = MessageType.SECONDARY.toString();

            // declare exchanges and queues
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(primaryName, true, false, false, null);
            channel.queueDeclare(secondaryName, true, false, false, null);
            channel.queueBind(primaryName, EXCHANGE_NAME, primaryName);
            channel.queueBind(secondaryName, EXCHANGE_NAME, secondaryName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void send(String message, String type) {
        try {
            // send a message to the exchange
            channel.basicPublish(EXCHANGE_NAME, type, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

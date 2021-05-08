package andras.patka.rabbitmq.demo.model;

import java.util.List;

/**
 * MessageType controls which exchange will handle the request.
 * Primary: Direct exchange, sends to primary channel.
 * Secondary: Direct exchange, sends to secondary channel.
 * All: Fanout exchange, sends to both channels.
 */
public enum MessageType {
    PRIMARY,
    SECONDARY,
    ALL;

    public static List<String> getMessageTypes() {
        return List.of(PRIMARY.toString(), SECONDARY.toString(), ALL.toString());
    }
}

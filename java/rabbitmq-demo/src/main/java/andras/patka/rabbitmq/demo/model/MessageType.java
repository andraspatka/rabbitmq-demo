package andras.patka.rabbitmq.demo.model;

import java.util.List;

public enum MessageType {
    PRIMARY,
    SECONDARY;

    public static List<String> getMessageTypes() {
        return List.of(PRIMARY.toString(), SECONDARY.toString());
    }
}

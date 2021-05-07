package andras.patka.rabbitmq.demo.model;

import lombok.Data;

@Data
public class PublishRequest {

    private String message;
    private String type;
}

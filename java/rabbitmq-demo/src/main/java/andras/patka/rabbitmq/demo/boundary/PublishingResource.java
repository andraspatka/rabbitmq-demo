package andras.patka.rabbitmq.demo.boundary;

import andras.patka.rabbitmq.demo.control.RabbitMqService;
import andras.patka.rabbitmq.demo.model.MessageType;
import andras.patka.rabbitmq.demo.model.PublishRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/publish")
public class PublishingResource {

    @Inject
    RabbitMqService rabbitMqService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response publish(PublishRequest publishRequest) {
        String type = publishRequest.getType().toUpperCase();
        String message = publishRequest.getMessage();

        if (!MessageType.getMessageTypes().contains(type)) {
            throw new BadRequestException("Type: " + publishRequest.getType() + " not supported. Try: 'primary' or 'secondary'");
        }

        rabbitMqService.send(message, type);

        return Response.ok("Message: " + message + " successfully transmitted to type: " + type).build();
    }
}

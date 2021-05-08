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

        String responseMessage = "";

        if (!MessageType.getMessageTypes().contains(type)) {
            throw new BadRequestException("Type: " + publishRequest.getType() + " not supported. Try: 'primary', 'secondary' or 'all'");
        }

        if (MessageType.ALL.toString().equals(type)) {
            rabbitMqService.send(message);
            responseMessage = "Message: " + message + " successfully transmitted to all binded queues.";
        } else {
            rabbitMqService.send(message, type);
            responseMessage = "Message: " + message + " successfully transmitted to type: " + type;
        }


        return Response.ok(responseMessage).build();
    }
}

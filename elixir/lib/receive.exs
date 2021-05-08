defmodule Receive do
  def wait_for_messages do
    receive do
      {:basic_deliver, payload, _meta} ->
        IO.puts " [x] Received #{payload} from queue: SECONDARY"
        wait_for_messages()
    end
  end
end

rabbitmqUser = System.get_env("RABBITMQ_USER", "user")
rabbitmqHost = System.get_env("RABBITMQ_HOST", "none")
rabbitmqPassword = System.get_env("RABBITMQ_PASSWORD", "none")

options = [host: rabbitmqHost, port: 5672, virtual_host: "/", username: rabbitmqUser, password: rabbitmqPassword]
{:ok, connection} = AMQP.Connection.open(options)
{:ok, channel} = AMQP.Channel.open(connection)
AMQP.Queue.declare(channel, "SECONDARY", [durable: true])
AMQP.Basic.consume(channel, "SECONDARY", nil, no_ack: true)
IO.puts " [*] Waiting for messages. To exit press CTRL+C, CTRL+C"

Receive.wait_for_messages()

defmodule Receive do
  def wait_for_messages do
    receive do
      {:basic_deliver, payload, _meta} ->
        IO.puts " [x] Received #{payload} from queue: SECONDARY"
        wait_for_messages()
    end
  end
end

options = [host: "localhost", port: 5672, virtual_host: "/", username: "user", password: "guest"]
{:ok, connection} = AMQP.Connection.open
{:ok, channel} = AMQP.Channel.open(connection)
AMQP.Queue.declare(channel, "SECONDARY")
AMQP.Basic.consume(channel, "SECONDARY", nil, no_ack: true)
IO.puts " [*] Waiting for messages. To exit press CTRL+C, CTRL+C"

Receive.wait_for_messages()

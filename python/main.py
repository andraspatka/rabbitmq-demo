#!/usr/bin/env python3
import pika

import os
import sys

USER = ""
PASSWORD = ""
HOST = ""


def main():
    credentials = pika.PlainCredentials(USER, PASSWORD)
    parameters = pika.ConnectionParameters(HOST, 5672, '/', credentials)

    print(f'Connecting to RabbitMQ host: {HOST}:5672 with user: {USER}')
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    print(f'Connection successful')

    def callback(ch, method, properties, body):
        print(f"Received {body} from queue: PRIMARY")

    channel.basic_consume(queue='PRIMARY', auto_ack=True, on_message_callback=callback)
    print('Waiting for messages.')
    channel.start_consuming()

if __name__ == '__main__':
    try:
        USER = os.environ["RABBITMQ_USER"]
        PASSWORD = os.environ["RABBITMQ_PASSWORD"]
        HOST = os.environ["RABBITMQ_HOST"]
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
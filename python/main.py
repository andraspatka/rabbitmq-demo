#!/usr/bin/env python3
import os
import sys
import logging

import pika

USER = ""
PASSWORD = ""
HOST = ""


def main():
    credentials = pika.PlainCredentials(USER, PASSWORD)
    parameters = pika.ConnectionParameters(HOST, 5672, '/', credentials)

    logging.info(f'Connecting to RabbitMQ host: {HOST}:5672 with user: {USER}')
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    logging.info(f'Connection successful')

    def callback(ch, method, properties, body):
        logging.info(f"Received {body} from queue: PRIMARY")

    channel.basic_consume(queue='PRIMARY', auto_ack=True, on_message_callback=callback)
    logging.info('Waiting for messages.')
    channel.start_consuming()


if __name__ == '__main__':
    USER = os.environ["RABBITMQ_USER"]
    PASSWORD = os.environ["RABBITMQ_PASSWORD"]
    HOST = os.environ["RABBITMQ_HOST"]
    main()


## Spring Boot Service with RabbitMQ Message Scheduling

This is a Spring Boot service that uses RabbitMQ to schedule messages with a delay for a specific queue. The service provides a REST API endpoint that allows clients to schedule a message with a delay by specifying the queue name, payload, and delay in milliseconds in the request body.

## API Endpoint

The API endpoint is a POST request to `/message`. The request body should be in JSON format and should include the following fields:

- `queueName`: The name of the queue where the message will be sent.
- `payload`: The payload of the message.
- `delayInMs`: The delay in milliseconds before the message should be processed.

Example request body:

```json
{
  "queueName": "myQueue",
  "payload": {
    "message": "Hello, world!"
  },
  "delayInMs": 5000
}
```

Sure! Here's the markdown code for the README:

css
Copy code
# Spring Boot Service with RabbitMQ Message Scheduling

This is a sample Spring Boot service that demonstrates how to use RabbitMQ to schedule messages with a delay. The service provides a REST API endpoint that allows clients to schedule a message with a delay by specifying the queue name, payload, and delay in milliseconds in the request body.

## API Endpoint

The API endpoint is a POST request to `/message`. The request body should be in JSON format and should include the following fields:

- `queueName`: The name of the queue where the message will be sent.
- `payload`: The payload of the message.
- `delayInMs`: The delay in milliseconds before the message should be processed.

Example request body:

```json
{
  "queueName": "myQueue",
  "payload": {
    "message": "Hello, world!"
  },
  "delayInMs": 5000
}

## Message Scheduling
When a message is scheduled, the service first validates the presence of the queue specified in the request body. If the queue does not exist, the service returns an error with 400 HTTP STATUS.

If the queue is existing , the service then creates a backoff queue with a name based on the specified delay(see pattern below), if it does not already exist. The backoff queue is used to delay the delivery of the message. There are no consumers for this queue so it acts as a parking lot queue to hold message with that specific delay till the TTL has elapsed.

The backoff queue is bound to a dead letter exchange using the x-dead-letter-exchange argument. When the message's TTL (time-to-live) expires, it will be routed to the dead letter exchange using the x-dead-letter-routing-key argument. This is set as the routing key for the processing queue The dead letter exchange will then route the message to the processing queue, where it will be consumed and sent to the appropriate queue mentioned in the original request body.


## Setup
To run the service, you will need to have RabbitMQ installed and running. You will also need to update the application.properties file with your RabbitMQ connection details. We need to build the service using command below.

> mvn clean install

This will create a jar in the /target folder of the repository. Next we can build a docker image using the below command. ( env is specified as dev in dockerfile )

> docker build -t rmq-scheduler .

This will create a docker image usign the rmq-scheduler tag , now we can use the docker-compose to run the service using below command.

> docker-compose up

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

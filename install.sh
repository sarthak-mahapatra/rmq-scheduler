#!/bin/bash

mvn clean install
docker build -t rmq-scheduler .
docker-compose up
# Use an existing image as the base image
FROM openjdk:11

# Set the working directory to /app
WORKDIR /app

# Copy the jar file to the working directory
COPY target/rmq-scheduler-*.jar /app/rmq-scheduler.jar

EXPOSE 8077

# Specify the command to run when the container starts
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "rmq-scheduler.jar"]

CMD ["echo", "Build Successfuly Completed!"]
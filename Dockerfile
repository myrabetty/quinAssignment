FROM adoptopenjdk/openjdk11:jre
VOLUME /tmp
ADD ./target/assignment-0.0.1-SNAPSHOT.jar app.jar
CMD [ "sh", "-c", "java -jar /app.jar" ]
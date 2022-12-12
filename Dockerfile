FROM openjdk:17-jdk-slim
ADD target/*.jar /home/lms-middleware-service-0.0.1-SNAPSHOT.jar
#CMD ["java", "-jar", "/home/lms-middleware-service-0.0.1-SNAPSHOT.jar"]

ENTRYPOINT exec java $JAVA_OPTS -jar /home/lms-middleware-service-0.0.1-SNAPSHOT.jar

#CMD ["java", "-jar", "/home/lms-middleware-service-0.0.1-SNAPSHOT.jar"]

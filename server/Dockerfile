FROM amazoncorretto:11
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} server.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /server.jar"]
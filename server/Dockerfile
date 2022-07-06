FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/server-0.0.1.jar server.jar
VOLUME /server
ENTRYPOINT ["java","-jar","/server.jar"]
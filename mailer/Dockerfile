FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/mailer-1.0-SNAPSHOT.jar mailer.jar
VOLUME /mailer
ENTRYPOINT ["java","-jar","mailer.jar"]
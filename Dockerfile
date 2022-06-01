FROM openjdk:17
VOLUME /tmp
COPY build/libs/*.war app.war
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.war"]
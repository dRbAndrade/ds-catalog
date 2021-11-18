FROM openjdk:17

WORKDIR /banana

COPY target/dscatalog-0.0.1-SNAPSHOT.jar /banana/springdocker.jar

ENTRYPOINT ["java", "-jar", "springdocker.jar"]
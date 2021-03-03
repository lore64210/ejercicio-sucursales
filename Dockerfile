FROM adoptopenjdk/openjdk11:jre-11.0.4_11
VOLUME /tmp
ARG JAR_FILE
COPY ./build/libs/sucursal-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
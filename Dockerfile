FROM adoptopenjdk:11-jre-hotspot as builder

LABEL maintainer "Caique Borges <caiquetgr@gmail.com>"
EXPOSE 8080
ARG JAR=target/*.jar
COPY ${JAR} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk:11-jre-hotspot

COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

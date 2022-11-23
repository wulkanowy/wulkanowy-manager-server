FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 3001
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/manager-server.jar

ENV PORT=3001
ENTRYPOINT ["java","-jar","/app/manager-server.jar"]

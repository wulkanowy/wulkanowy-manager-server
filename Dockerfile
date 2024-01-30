FROM gradle:8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 3001
RUN mkdir /app
COPY --from=build /home/gradle/src/.env /home/gradle/src/build/libs/*.jar /app/

ENV PORT=3001
ENTRYPOINT ["java","-jar","/app/manager-server-all.jar"]

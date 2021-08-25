FROM gradle:jdk14 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:14-alpine
EXPOSE 8080
RUN mkdir /java/
RUN apk add --no-cache --upgrade bash
COPY --from=builder /home/gradle/src/build/output/application.jar /java/

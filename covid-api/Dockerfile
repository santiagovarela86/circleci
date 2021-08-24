FROM gradle:jdk14 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build
COPY ./wait-for-it.sh /home/gradle/src/build/output

FROM openjdk:14-alpine
EXPOSE 8080
RUN mkdir /java/
RUN apk add --no-cache --upgrade bash
COPY --from=builder /home/gradle/src/build/output/application.jar /java/
COPY --from=builder /home/gradle/src/build/output/wait-for-it.sh /java/
RUN chmod +x /java/wait-for-it.sh
ARG BUILD_spring_jpa_hibernate_ddl_auto=update
ENV spring.jpa.hibernate.ddl-auto=$BUILD_spring_jpa_hibernate_ddl_auto
ARG BUILD_spring_jpa_hibernate_use_new_id_generator_mappings=false
ENV spring.jpa.hibernate.use-new-id-generator-mappings=$BUILD_spring_jpa_hibernate_use_new_id_generator_mappings

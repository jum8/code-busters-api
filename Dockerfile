FROM openjdk:17.0-slim
# Agrega el usuario y el grupo
RUN groupadd devopsc \
    && useradd -g devopsc javams
USER javams:devopsc
ENV JAVA_OPTS=""
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

VOLUME /tmp
EXPOSE 7280
ENTRYPOINT ["java","-jar","app.jar"]
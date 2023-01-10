FROM dev-docker-registry.tecnotree.com/mirror/registry.docker.io/adoptopenjdk/openjdk11:jre-latest

LABEL maintainer="devopsCICD <DL-PE-CICD@tecnotree.com>" \
      vendor=Tecnotree

# set configurable USERNAME group
ARG UID=860
ARG GID=860
ARG USERNAME=spring
ARG GROUPNAME=spring
ARG USER_HOME=/usr/local/app

RUN apk add --no-cache libxext-dev libxext-doc libxtst \
 freetype fontconfig xproto libx11 libxft libxext libxi xterm xhost

# RUN xhost + local:root

# create user group and user
RUN addgroup --system --gid ${GID} ${GROUPNAME} && \
    adduser --system --home ${USER_HOME} --shell /bin/sh --uid ${UID} --ingroup ${GROUPNAME} --disabled-password ${USERNAME}

# setting workdir
WORKDIR ${USER_HOME}

ARG SERVER_PORT=8085
EXPOSE ${SERVER_PORT}

# RELEASE is reset with git describe in CI
ARG RELEASE="5.0.0-SNAPSHOT"
ARG SPRINGBOOT_APP_JAR=digital-wallet-payment-services.jar

LABEL service=digital-wallet-payment-service \
      release_tags="${RELEASE}"

# set default environment properties for the springboot
ENV SERVER_PORT=${SERVER_PORT} \
    SPRINGBOOT_APP_JAR=${SPRINGBOOT_APP_JAR} \
    LOG_LEVEL=INFO \
    JAVA_OPTS='-Xms1g -Xmx1g' \
    TOMCAT_BASE=${USER_HOME} \
    TOMCAT_MAX_CONNECTIONS=100 \
    TOMCAT_MAX_THREADS=10 \
    TOMCAT_MIN_SPARE_THREADS=5 \
    SERVER_HOST=localhost \
    CONFIG_DIR=/usr/ \
    CONFIG_VERSION=v1 \
    DISPLAY=:0

# copy springboot application
COPY --chown=860:860 digital-wallet/digital-wallet-payment-services/target/${SPRINGBOOT_APP_JAR} ${USER_HOME}

# switch to non-root app user
USER ${UID}:${GID}

# entrypoint
ENTRYPOINT exec java $JAVA_OPTS -jar ${SPRINGBOOT_APP_JAR}

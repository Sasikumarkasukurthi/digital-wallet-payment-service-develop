#!/bin/sh

# exit on unset variables and non-zero return code
set -eu

# provision for additional entrypoint configuration, healthcheck on backing database, peer cluster member discovery


# run springboot application with configurable arguments
cd ${USER_HOME} && exec java $JAVA_OPTS -jar ${SPRINGBOOT_APP_JAR} --server.port=${SERVER_PORT} $@

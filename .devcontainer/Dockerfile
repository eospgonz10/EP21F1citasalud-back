FROM openjdk:17-jdk-slim

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends postgresql-client git maven \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Crear el usuario vscode
RUN useradd -ms /bin/bash vscode

USER vscode
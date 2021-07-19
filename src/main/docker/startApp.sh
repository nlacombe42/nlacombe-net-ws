#!/bin/bash

command="java -Xms100m -Xmx280m"
command+=" -Djava.security.egd=file:///dev/urandom"
command+=" -Dspring.profiles.active=native"
command+=" -Dspring.config.location=file:///app/config/application.yaml"
command+=" -Dserver.port=${PORT}"
command+=" -jar app.jar"

echo "$command"
$command

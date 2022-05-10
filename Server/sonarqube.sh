#!/bin/bash

mvn clean verify sonar:sonar -Dsonar.projectName=Smart-Ad-Platform-Server -Dsonar.projectKey=Smart-Ad-Platform-Server -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=P@ssword123

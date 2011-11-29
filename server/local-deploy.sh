#!/bin/bash

java -jar runtime/grizzly-http-servlet-deployer-1.9.41.jar \
  -a target/ca.wasabistudio.ca-*.war --context=/


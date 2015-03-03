#!/usr/bin/env bash
mvn clean package source:jar javadoc:jar test install deploy -DaltDeploymentRepository=archiva.snapshots::default::http://maven.n.miliao.com:8081/nexus/content/repositories/snapshots

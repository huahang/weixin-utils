#!/usr/bin/env bash
mvn clean package source:jar javadoc:jar test install deploy -DaltDeploymentRepository=archiva.snapshots::default::http://nexus.d.xiaomi.net:8081/nexus/content/repositories/snapshots

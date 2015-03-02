#!/usr/bin/env bash
mvn clean package source:jar javadoc:jar test install

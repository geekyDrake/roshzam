#!/bin/bash
echo "Installing audio-fingerprint to local Maven repo..."
mvn install:install-file \
  -Dfile=lib/audio-fingerprint-1.0.0-jar-with-dependencies.jar \
  -DgroupId=io.honerlaw \
  -DartifactId=audio-fingerprint \
  -Dversion=1.0.0 \
  -Dpackaging=jar
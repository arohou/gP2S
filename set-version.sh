#!/bin/bash

export version=${1}
export MESSAGE="Please provide version number in format [0-9]+.[0-9]+.[0-9]+[-SNAPSHOT]"

# check if version parameter is specified
if [ -z ${version:+x} ]; then 
	echo ${MESSAGE}
	exit 1
fi

# check if version has correct format
if [[ ! "${version}" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-SNAPSHOT)?$ ]]; then
	echo "Invalid format. ${MESSAGE}"
	exit 2
fi

# update version in pom files
mvn versions:set -DnewVersion=${version} versions:commit -q

# update first version occurrence
cd frontend && npm version ${version}

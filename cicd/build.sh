#!/bin/bash
echo "Building docker image"
docker build --build-arg PROFILE="$PROFILE" -f $(pwd)/Dockerfile -t "$CI_REGISTRY"/"$CI_REGISTRY_USER"/"$service_name":"$tag" .
echo "Pushing docker image to registry"
docker push "$CI_REGISTRY"/"$CI_REGISTRY_USER"/"$service_name":"$tag"
echo "Pushing docker image to registry successfully"
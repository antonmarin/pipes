SHELL = /bin/sh
.DEFAULT_GOAL=help
GOOS=$(shell uname -s | tr '[:upper:]' '[:lower:]')
.PHONY: help clear build run publish
help: #? help me
	@printf "\e[34;01mAvailable targets\033[0m\n"
	@awk '/^@?[a-zA-Z\-_0-9]+:/ { \
		nb = sub( /^#\? /, "", helpMsg ); \
		if(nb == 0) { \
			helpMsg = $$0; \
			nb = sub( /^[^:]*:.* #\? /, "", helpMsg ); \
		} \
		if (nb) \
			printf "\033[1;31m%-" width "s\033[0m %s\n", $$1, helpMsg; \
	} \
	{ helpMsg = $$0 }' \
	$(MAKEFILE_LIST) | column -ts:

# decorations
COLOR_NONE="\\033[0m"
COLOR_BLUE="\\033[34m"
COLOR_CYAN="\\033[36m"
COLOR_GREEN="\\033[32m"
COLOR_YELLOW="\\033[33m"
COLOR_ORANGE="\\033[43m"
COLOR_RED="\\033[31m"

DOCKER_IMAGE:=autoget
DOCKER_TAG_WIP:=$(DOCKER_IMAGE):latest
DOCKER_TEST_NETWORK:="test-network"
DOCKER_DIND_PORT:=2375
clear:
	docker stop dind; \
	docker network rm $(DOCKER_TEST_NETWORK) || exit 0;
build: clear #? build image
	docker network create $(DOCKER_TEST_NETWORK) \
	&& docker run --rm --privileged -d --name dind -p $(DOCKER_DIND_PORT):2375 \
        --network $(DOCKER_TEST_NETWORK) --network-alias dind \
        -e DOCKER_TLS_CERTDIR="" docker:dind \
	&& DOCKER_BUILDKIT=0 docker build \
		--network=$(DOCKER_TEST_NETWORK) --build-arg "DOCKER_HOST=tcp://dind:$(DOCKER_DIND_PORT)" \
		-t "$(DOCKER_TAG_WIP)" .; \
	$(MAKE) clear;
run: #? run once latest wip image
	docker run --rm --env "LOGGING_DOMAIN_ROOT_LEVEL=DEBUG" "$(DOCKER_TAG_WIP)" --once

publish: PUBLISH_DATE:=$(shell date -I date)
publish: #? publish image to deploy to server
	docker tag "$(DOCKER_TAG_WIP)" "antonmarin/$(DOCKER_IMAGE):$(PUBLISH_DATE)" \
	&& docker push "antonmarin/$(DOCKER_IMAGE):$(PUBLISH_DATE)"
	@echo "$(COLOR_CYAN)Published antonmarin/$(DOCKER_IMAGE):$(PUBLISH_DATE)$(COLOR_NONE)"

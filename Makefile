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
DOCKER_IMAGE_PUBLIC:=antonmarin/$(DOCKER_IMAGE)
TAG:=$(shell git rev-parse --short HEAD)
DOCKER_TEST_NETWORK:=buildnet
DOCKER_DIND_PORT:=2375
BUILDX:="mybuilder"
DOCKER_CACHE_TO:=--load --provenance=false

build: prepare-buildkit #? build image
	DIND_IP=$(shell docker inspect -f '{{.NetworkSettings.Networks.$(DOCKER_TEST_NETWORK).IPAddress}}' dind) \
	&& docker buildx build --progress plain \
		--builder $(BUILDX) \
		--platform linux/amd64 \
		--cache-from type=registry,ref="$(DOCKER_IMAGE_PUBLIC):cache" \
		$(DOCKER_CACHE_TO) \
		--build-arg "DOCKER_HOST=tcp://$$DIND_IP:$(DOCKER_DIND_PORT)" \
		-t "$(DOCKER_IMAGE):$(TAG)" .

clean:
	docker buildx rm $(BUILDX); \
	docker stop dind; \
	docker network rm $(DOCKER_TEST_NETWORK) || exit 0;

prepare-buildkit:
	docker buildx create \
		--name $(BUILDX) \
		--driver docker-container \
		--driver-opt "network=$(DOCKER_TEST_NETWORK)" \
		--use \
	&& docker network create $(DOCKER_TEST_NETWORK) \
	&& docker run --rm --privileged -d --name dind \
		-p $(DOCKER_DIND_PORT):2375 \
		--network $(DOCKER_TEST_NETWORK) --network-alias dind \
		-e DOCKER_TLS_CERTDIR="" docker:dind || true

publish: PUBLIC_TAG:=$(shell date -Idate)
publish: #? publish image to deploy to server
	docker tag "$(DOCKER_IMAGE):$(TAG)" "$(DOCKER_IMAGE_PUBLIC):$(PUBLIC_TAG)" \
	&& docker push "$(DOCKER_IMAGE_PUBLIC):$(PUBLIC_TAG)"
	@echo "$(COLOR_CYAN)Published $(DOCKER_IMAGE_PUBLIC):$(PUBLIC_TAG)$(COLOR_NONE)"

release: #? run manually to tag revision to publish. Write to master required
	@read -p "Enter release version by pattern YYYY-0M-0D(-optionalIncrement) calver.org: " NAME; \
	echo "tagging $$NAME"; \
	git tag $$NAME && git push origin tag $$NAME && echo "Tag $$NAME pushed";

run: #? run once latest wip image
	docker run --rm --env-file=.env --env "LOGGING_DOMAIN_ROOT_LEVEL=DEBUG" "$(DOCKER_IMAGE):$(TAG)" --once

update-cache: DIND_IP:=$(docker inspect -f '{{range .NetworkSettings.Networks.$(DOCKER_TEST_NETWORK)}}{{.IPAddress}}{{end}}' dind)
update-cache: prepare-buildkit
	$(MAKE) DOCKER_CACHE_TO="--cache-to type=registry,ref=$(DOCKER_IMAGE_PUBLIC):cache,mode=max" build

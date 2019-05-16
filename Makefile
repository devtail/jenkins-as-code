get-tools-linux: ARCH=linux
get-tools-mac: ARCH=darwin

.PHONY: help

help: ## Prints help for targets with comments
	@grep -E '^[a-zA-Z0-9.\ _-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

get-tools-linux get-tools-mac: ## Pull dep-less binaries (minikube, kubectl, helm) to local bin/
	# TODO: verify checksums ..
	curl -Lo bin/minikube https://storage.googleapis.com/minikube/releases/v1.0.0/minikube-$(ARCH)-amd64 && chmod +x bin/minikube
	curl -Lo bin/kubectl https://storage.googleapis.com/kubernetes-release/release/v1.14.0/bin/$(ARCH)/amd64/kubectl && chmod +x bin/kubectl
	curl -L https://storage.googleapis.com/kubernetes-helm/helm-v2.13.1-$(ARCH)-amd64.tar.gz | tar -xz -C bin/ && mv bin/$(ARCH)-amd64/helm bin/ && rm -r bin/$(ARCH)-amd64

minikube-start: ## Start and prepare minikube with ingress addon
	bin/minikube start --extra-config=apiserver.service-node-port-range=80-32000
	bin/minikube addons enable ingress
	bin/helm init --history-max 100

helm-deploy: ## Deploy jenkins to minikube
	bin/helm upgrade --wait --install jenkins resources/helm

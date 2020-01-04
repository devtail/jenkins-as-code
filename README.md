[![Docker Repository on Quay.io](https://quay.io/repository/devtail/jenkins-as-code/status "Docker Repository on Quay.io")](https://quay.io/repository/devtail/jenkins-as-code)

# jenkins-as-code

**[Demo] Bootstrapping Jenkins:**
<p align="center">
  <img src="https://fishi.devtail.io/content-images/jenkins-bootstrap-700px.gif">
</p>

This repository contains a Jenkins-as-Code approach. 
Everything is tested and running with Jenkins `2.204.1` on minikube `v1.0.0` (the .gif you above was done with an older version). 
The setup is based on docker, helm and git so it can be easily applied in different infrastructures.
Plugins and minimum setup are pre-baked inside a docker image. 
A configuration and seeding pipeline provisions Jenkins with configuration code from a central git repository. 
Configuration includes: agents on demand (with terraform), slack, github, github-oauth, security settings, theming, ... 

## Running locally

The following files with your secrets have to be created to run this prototype:

```
resources/helm/
|-- secret-files
    |-- default-setup-password          # pre-baked setup user password
    |-- default-setup-user              # pre-baked setup user name
    |-- deploy-key-shared-library       # private ssh deploy key
    |-- deploy-key-shared-library.pub   # public ssh deploy key
    |-- github-ci-password              # GitHub Jenkins user password
    |-- github-ci-token                 # GitHub Jenkins user access token
    |-- github-ci-user                  # GitHub Jenkins user name
    |-- github-oauth-client-id
    |-- github-oauth-client-secret
    |-- slack-token
    |-- ssh-agent-access-key            # private ssh key for agent access
    |-- ssh-agent-access-key.pub        # public ssh key for agent access
    `-- terraform-config                # Terraform backend configs and secrets
        |-- aws-agent-network.backend.config
        |-- aws-agent-network.tfvars
        |-- aws-agent-vms.backend.config
        `-- aws-agent-vms.tfvars
```

After you have placed your secret files you can run:

```
make get-tools-(linux|mac)
make minikube-start
make deploy-helm
```

This should open Jenkins on [http://192.168.99.100/](http://192.168.99.100/).

## On-Demand Agents

The code base also supports on-demand agents with custom terraform bootstrapping/destroy pipelines. 
A demo can be found [here](resources/README.md)

## Detailed Explanations

The following blog entries describe in more detail how this works:

- [Jenkins-as-Code Part I | Initial Setup](https://fishi.devtail.io/weblog/2019/01/06/jenkins-as-code-part-1/)
- [Jenkins-as-Code Part II | Configuration](https://fishi.devtail.io/weblog/2019/01/12/jenkins-as-code-part-2/)
- [Jenkins-as-Code Part III | JobDSL](https://fishi.devtail.io/weblog/2019/02/09/jenkins-as-code-part-3/)

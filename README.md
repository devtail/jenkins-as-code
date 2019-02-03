[![Docker Repository on Quay.io](https://quay.io/repository/devtail/jenkins-as-code/status "Docker Repository on Quay.io")](https://quay.io/repository/devtail/jenkins-as-code)

# jenkins-as-code

**[Demo] Bootstrapping Jenkins:**
<p align="center">
  <img src="https://fishi.devtail.io/content-images/jenkins-bootstrap.gif">
</p>

This repository contains a Jenkins-as-Code approach. 
Everything is tested and running on minikube `v0.33.1`. 
The setup is based on docker, helm and git so it can be easily applied in different infrastructures.
Plugins and minimum setup are pre-baked inside a docker image. 
A configuration and seeding pipeline provisions Jenkins with configuration code from a central git repository. 
Configuration includes: agents on demand (with terraform), slack, github, github-oauth, security settings, theming, ... 

## Detailed Explanation

The following blog entries describe in more detail how this works:

- [Jenkins-as-Code Part I | Initial Setup](https://fishi.devtail.io/weblog/2019/01/06/jenkins-as-code-part-1/)
- [Jenkins-as-Code Part II | Configuration](https://fishi.devtail.io/weblog/2019/01/12/jenkins-as-code-part-2/)

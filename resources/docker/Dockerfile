FROM jenkins/jenkins:2.204.1

ENV ARCH=linux_amd64 \
    VAULT_VERSION=1.3.1 \
    TERRAFORM_VERSION=0.12.18 

# Delay for CasC + disable install wizard
ENV JAVA_OPTS="-Dio.jenkins.plugins.casc.ConfigurationAsCode.initialDelay=9000 -Djenkins.install.runSetupWizard=false -Dorg.jenkinsci.main.modules.sshd.SSHD.hostName=127.0.0.1"

# JCasC Plugin pointer to config/secret values
ENV SECRETS="/var/jenkins_home/"

USER root

# Install deps
RUN apt-get update -y \
  && apt-get install -y build-essential rsync

# Add Vault client
RUN curl -sL https://releases.hashicorp.com/vault/${VAULT_VERSION}/vault_${VAULT_VERSION}_${ARCH}.zip -o /tmp/vault_${VAULT_VERSION}_${ARCH}.zip \
  && curl -sL https://releases.hashicorp.com/vault/${VAULT_VERSION}/vault_${VAULT_VERSION}_SHA256SUMS -o /tmp/vault_${VAULT_VERSION}_SHA256SUMS \
  && export CUR_DIR=$(pwd) \
  && cd /tmp \
  && sha256sum -c vault_${VAULT_VERSION}_SHA256SUMS 2>&1 | grep OK \
  && cd ${CUR_DIR}

RUN unzip /tmp/vault_${VAULT_VERSION}_${ARCH}.zip -d /bin
RUN rm -f /tmp/vault_${VAULT_VERSION}_${ARCH}.zip \
  && rm -f /tmp/vault_${VAULT_VERSION}_SHA256SUMS

# Add Terraform client
RUN curl -sL https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_${ARCH}.zip -o /tmp/terraform_${TERRAFORM_VERSION}_${ARCH}.zip \
  && curl -sL https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_SHA256SUMS -o /tmp/terraform_${TERRAFORM_VERSION}_SHA256SUMS \
  && export CUR_DIR=$(pwd) \
  && cd /tmp \
  && sha256sum -c terraform_${TERRAFORM_VERSION}_SHA256SUMS 2>&1 | grep OK \
  && cd ${CUR_DIR}

RUN unzip /tmp/terraform_${TERRAFORM_VERSION}_${ARCH}.zip -d /bin
RUN rm -f /tmp/terraform_${TERRAFORM_VERSION}_${ARCH}.zip \
  && rm -f /tmp/terraform_${TERRAFORM_VERSION}_SHA256SUMS

USER jenkins

# Add minimum jenkins setup
ADD init.groovy.d /usr/share/jenkins/ref/init.groovy.d
ADD dsl /usr/share/jenkins/ref/dsl
COPY scriptApproval.xml /var/jenkins_home/scriptApproval.xml

# Install plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt


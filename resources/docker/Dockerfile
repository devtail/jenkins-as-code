FROM jenkins/jenkins:2.150.2

ENV ARCH=linux_amd64 \
    VAULT_VERSION=1.0.2 \
    TERRAFORM_VERSION=0.11.11 \
    JENKINS_VERSION=2.150.2

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Add Vault client
USER root
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
USER root
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

# Disable wizards
RUN echo "${JENKINS_VERSION}" > /var/jenkins_home/jenkins.install.InstallUtil.lastExecVersion
RUN echo "${JENKINS_VERSION}" > /var/jenkins_home/jenkins.install.UpgradeWizard.state

node('master') {
  stage('Checkout') {
    // Clean workspace and checkout shared library repository on the jenkins master
    cleanWs()
    checkout scm
  }

  stage('Configuration') {
    // set config file in master
    sh('cp /var/jenkins_home/workspace/Admin/Configure/resources/config/configuration-as-code-plugin/jenkins.yaml /var/jenkins_home/jenkins.yaml')

    // run configuration from config file
    load('resources/config/groovy/triggerConfigurationAsCodePlugin.groovy')

    // set public key for bootstrapping user
    load('resources/config/groovy/userPublicKeys.groovy')

    // set the timezone
    load('resources/config/groovy/timezone.groovy')

    // copy agent boostrapping automation
    sh('rsync -rl /var/jenkins_home/workspace/Admin/Configure/resources/terraform/ /var/jenkins_home/agent-bootstrapping/')
    sh('ln -sfn /var/jenkins_home/agent-bootstrapping-terraform-config /var/jenkins_home/agent-bootstrapping/config')
  }

  stage('Deploy Agent Networks') {
    ansiColor('xterm') {
      sh('cd /var/jenkins_home/agent-bootstrapping && make deploy-agent-network')
    }
  }

  stage('Job Seeding') {
    jobDsl(targets: 'resources/jobDSL/*.groovy', sandbox: false)
  }
}

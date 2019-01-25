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
    // set the timezone
    load('resources/config/groovy/timezone.groovy')
  }

  stage('Job Seeding') {
    // seed the jobs
    jobDsl(targets: 'resources/jobDSL/*.groovy', sandbox: false)
  }
}

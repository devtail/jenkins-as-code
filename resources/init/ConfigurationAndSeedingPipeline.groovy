node('master') {
  stage('Checkout') {
    // Clean workspace and checkout shared library repository on the jenkins master
    cleanWs()
    checkout scm
  }

  stage('Credentials') {
    // groovy script to configure and load credentials such as ssh keys
    load('resources/config/credentials.groovy')
  }

  stage('Auth') {
    // groovy script to configure the authentication (e.g., OAuth)
    load('resources/config/auth.groovy')
  }

  //stage('Slaves') {
  //  // groovy script to configure the jenkins slaves
  //  load('resources/config/slaves.groovy')
  //}

  stage('Shared Libraries') {
    // groovy script to configure the shared library repository as a global shared library within jenkins
    load('resources/config/globalSharedLibrary.groovy')
  }

  stage('General Settings') {
    // groovy script for some general settings (e.g., UI theme or slack)
    load('resources/config/securitySettings.groovy')
    load('resources/config/slack.groovy')
    load('resources/config/theme.groovy')
    load('resources/config/github.groovy')
    load('resources/config/timezone.groovy')
    load('resources/config/baseURL.groovy')
    load('resources/config/globalEnvVars.groovy')
  }

  stage('Seed') {
    // seed the jobs
    jobDsl(targets: 'resources/jobDSL/*.groovy', sandbox: false)
  }
}

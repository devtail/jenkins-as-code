#!groovy

/*
 * This script is designated for the init.groovy.d 
 * directory to be executed at startup time of the 
 * Jenkins instance. This script requires the jobDSL
 * Plugin. Tested with job-dsl:1.70
 */

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm


// Add deploy key for the centrally shared pipeline and configuration repository
def domain = Domain.global()
def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
def keyFileContents = new File("/var/jenkins_home/jenkins-ssh-keys/deploy-key-shared-library").text
def privateKey = new BasicSSHUserPrivateKey(
  CredentialsScope.GLOBAL,
  "deploy-key-shared-library",
  "root",
  new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(keyFileContents),
  "",
  "SSH key for shared-library"
)
store.addCredentials(domain, privateKey)

// Create the configuration pipeline from a jobDSL script
def jobDslScript = new File('/var/jenkins_home/dsl/ConfigurationAndSeedingPipelineDSL.groovy')
def workspace = new File('.')
def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
new DslScriptLoader(jobManagement).runScript(jobDslScript.text)

// Disable Wizards
if(Jenkins.instance.getSecurityRealm().getClass().getSimpleName() == 'None') {
    def instance = Jenkins.getInstance()
    def setupUser = new File("/var/jenkins_home/jenkins-basic-auth-credentials/default-setup-user").text.trim()
    def setupPass = new File("/var/jenkins_home/jenkins-basic-auth-credentials/default-setup-password").text.trim()

    def hudsonRealm = new HudsonPrivateSecurityRealm(false)
    instance.setSecurityRealm(hudsonRealm)
    def user = instance.getSecurityRealm().createAccount(setupUser, setupPass)
    user.save()

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.setAllowAnonymousRead(false)
    instance.setAuthorizationStrategy(strategy)

    instance.save()

    println("SetupWizard Disabled")
}

#!groovy

/*
 * This script loads different kinds 
 * of credentials from files into the 
 * Jenkins credential store. 
 *
 * https://gist.github.com/fishi0x01/7c2d29afbaa0f16126eb4d4b35942f76#file-credentials-groovy
 */

import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import org.jenkinsci.plugins.plaincredentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import hudson.util.Secret

///////////////////
// Helper functions
///////////////////
def getStore() {
    return Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
}

def getContent(filePath) {
    return new File(filePath).text
}

// This function reads the contents of a key file and returns
// a Jenkins SSH private key object with the given user as owner
def getSSHKeyCredential(id, path, user) {
    return new BasicSSHUserPrivateKey(
        CredentialsScope.GLOBAL,
        id,
        user,
        new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(getContent(path)),
        "",
        "SSH key ${id}"
    )
}

// Get master credential store
domain = Domain.global()

//////////////////////////////
// Add username/password pairs
//////////////////////////////
userPasswords = [
    [id: 'github-ci-user', description: 'GitHub CI User Credentials', userNameFile: '/var/jenkins_home/jenkins-basic-auth-credentials/github-ci-user', userPasswordFile: '/var/jenkins_home/jenkins-basic-auth-credentials/github-ci-password'], 
]

for(userPassword in userPasswords) {
    Credentials cred = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, userPassword.id, userPassword.description, getContent(userPassword.userNameFile), getContent(userPassword.userPasswordFile))
    getStore().addCredentials(domain, cred)
}

/////////////
// Add tokens
/////////////
secretTokens = [
    [id: 'slack-token', description: 'Slack Token', tokenFile: '/var/jenkins_home/jenkins-tokens/slack-token'],
    [id: 'github-ci-token', description: 'Github CI User Token', tokenFile: '/var/jenkins_home/jenkins-tokens/github-ci-token'],
    [id: 'github-oauth-client-id', description: 'Github OAuth Client ID', tokenFile: '/var/jenkins_home/jenkins-tokens/github-oauth-client-id'],
    [id: 'github-oauth-client-secret', description: 'Github OAuth Client Secret', tokenFile: '/var/jenkins_home/jenkins-tokens/github-oauth-client-secret'],
]

for(secretToken in secretTokens) {
    Credentials token = (Credentials) new StringCredentialsImpl(CredentialsScope.GLOBAL, secretToken.id, secretToken.description, Secret.fromString(getContent(secretToken.tokenFile)))
    getStore().addCredentials(domain, token)
}

///////////////
// Add ssh keys
///////////////
sshKeys = [
    [id: 'ssh-global-shared-library', path: '/var/jenkins_home/jenkins-ssh-keys/deploy-key-shared-library', user: 'root'],
]

for(sshKey in sshKeys) {
    getStore().addCredentials(domain, getSSHKeyCredential(sshKey.id, sshKey.path, sshKey.user))
}

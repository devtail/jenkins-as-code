#!groovy

/*
 * This script configures Github OAuth access 
 * in Jenkins. It uses a global authorization 
 * Matrix strategy as authorization configurtion.
 * This script requires the Gibhub Authentication 
 * plugin (github-oauth) to be installed. It is 
 * tested with github-oauth:0.29
 */

import hudson.security.SecurityRealm
import org.jenkinsci.plugins.GithubSecurityRealm
import jenkins.model.*
import hudson.security.*

// Setup OAUTH Realm
String githubWebUri = GithubSecurityRealm.DEFAULT_WEB_URI
String githubApiUri = GithubSecurityRealm.DEFAULT_API_URI
String oauthScopes = 'read:org,user:email' //GithubSecurityRealm.DEFAULT_OAUTH_SCOPES
String clientID = System.getenv()['GITHUB_OAUTH_CLIENT_ID'] // TODO: get from credential store
String clientSecret = System.getenv()['GITHUB_OAUTH_CLIENT_SECRET'] // TODO: get from credential store

SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)
Jenkins.instance.setSecurityRealm(github_realm)

// Create global authorization matrix
def strategy = new GlobalMatrixAuthorizationStrategy()

// Give admin access to fishi0x01 user
strategy.add(Jenkins.ADMINISTER, "fishi0x01")

// Give admin access to my_team_name
strategy.add(Jenkins.ADMINISTER, "devtail*Admin")

// wrap up
Jenkins.instance.setAuthorizationStrategy(strategy)
Jenkins.instance.save()

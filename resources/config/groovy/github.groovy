#!groovy

/*
 * This script configures the GitHub Plugin.
 * Requires the GitHub Plugin to be installed.
 * Tested with github:1.29.3
 */

import jenkins.model.Jenkins
import org.jenkinsci.plugins.github.config.GitHubPluginConfig
import org.jenkinsci.plugins.github.config.GitHubServerConfig

def githubConfig = new GitHubServerConfig("github-ci-token") // credential ID for our user token for the GitHub CI User
githubConfig.setManageHooks(true)
githubConfig.setName("GitHub")
def github = Jenkins.instance.getExtensionList(GitHubPluginConfig.class)[0]
github.setConfigs([
  githubConfig,
])
github.save()

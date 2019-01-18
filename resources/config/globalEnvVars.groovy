#!groovy

/*
 * This script configures global environment variables in Jenkins
 */

import jenkins.model.Jenkins
import hudson.EnvVars;
import hudson.slaves.EnvironmentVariablesNodeProperty;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.util.DescribableList;

def createGlobalEnvironmentVariables(String key, String value){

    Jenkins instance = Jenkins.getInstance();

    DescribableList<NodeProperty<?>, NodePropertyDescriptor> globalNodeProperties = instance.getGlobalNodeProperties();
    List<EnvironmentVariablesNodeProperty> envVarsNodePropertyList = globalNodeProperties.getAll(EnvironmentVariablesNodeProperty.class);

    EnvironmentVariablesNodeProperty newEnvVarsNodeProperty = null;
    EnvVars envVars = null;

    if ( envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0 ) {
        newEnvVarsNodeProperty = new hudson.slaves.EnvironmentVariablesNodeProperty();
        globalNodeProperties.add(newEnvVarsNodeProperty);
        envVars = newEnvVarsNodeProperty.getEnvVars();
    } else {
        envVars = envVarsNodePropertyList.get(0).getEnvVars();
    }
    envVars.put(key, value)
    instance.save()
}

createGlobalEnvironmentVariables('DEBUG', 'True')

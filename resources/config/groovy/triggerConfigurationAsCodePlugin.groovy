#! groovy

import jenkins.model.Jenkins

// trigger configuration
def jcacPlugin = Jenkins.instance.getExtensionList(io.jenkins.plugins.casc.ConfigurationAsCode.class).first()
jcacPlugin.configure()

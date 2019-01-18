#!groovy

/*
 * Configure general security settings.
 */
import jenkins.model.Jenkins
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.security.s2m.AdminWhitelistRule


// Disable Remote CLI
Jenkins.instance.getDescriptor("jenkins.CLI").get().setEnabled(false)

// CSRF Issuer
if(Jenkins.instance.getCrumbIssuer() == null) {
    Jenkins.instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
}

// agent to master security subsystem
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

// sshd settings
def sshDesc = Jenkins.instance.getDescriptor("org.jenkinsci.main.modules.sshd.SSHD")
sshDesc.setPort(6666)
sshDesc.getActualPort()
sshDesc.save()

Jenkins.instance.save()

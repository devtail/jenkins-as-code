#!groovy

/*
 * Configure pipeline shared libraries in the global Jenkins configuration.
 * This will safely compare configured libraries and only overwrite the global
 * shared library config if changes have been made.
 * workflow-cps-global-lib:2.12
 *
 * Source: https://github.com/thbkrkr/jks/blob/master/init.groovy.d/11-configure-pipeline-global-shared.groovy
 */
import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource
import jenkins.plugins.git.traits.BranchDiscoveryTrait
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

List libraries = [] as ArrayList

def remote = 'git@github.com:devtail/jenkins-as-code.git'
def credentialsId = 'deploy-key-shared-library'

name = 'devtail-ci'
defaultVersion = 'master'

if (remote != null) {

    def scm = new GitSCMSource(remote)
    if (credentialsId != null) {
        scm.credentialsId = credentialsId
    }

    scm.traits = [new BranchDiscoveryTrait()]
    def retriever = new SCMSourceRetriever(scm)

    def library = new LibraryConfiguration(name, retriever)
    library.defaultVersion = defaultVersion
    library.implicit = false
    library.allowVersionOverride = true
    library.includeInChangesets = false

    libraries << library

    def global_settings = Jenkins.instance.getExtensionList(GlobalLibraries.class)[0]
    global_settings.libraries = libraries
    global_settings.save()
    println 'Configured Pipeline Global Shared Libraries:\n    ' + global_settings.libraries.collect { it.name }.join('\n    ')
}

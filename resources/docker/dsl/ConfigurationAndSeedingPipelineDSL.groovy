#!groovy

folder('Admin') {
    description('Folder containing configuration and seed jobs')
}

pipelineJob("Admin/Configure") {
    parameters {
        gitParam('revision') {
            type('BRANCH_TAG')
            sortMode('ASCENDING_SMART')
            defaultValue('origin/master')
        }
    }

    triggers {
        githubPush()
    }

    logRotator {
        numToKeep(20)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github("devtail/jenkins-as-code", "ssh")
                        credentials("deploy-key-shared-library")
                    }

                    branch('$revision')
                }
            }
            
            scriptPath('resources/init/ConfigurationAndSeedingPipeline.groovy')
        }
    }
}


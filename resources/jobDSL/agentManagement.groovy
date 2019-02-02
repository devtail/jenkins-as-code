pipelineJob("Admin/AWSAgentBootstrap") {

    parameters {
        stringParam('agentID', '', '') 
    }

    logRotator {
        numToKeep(50)
    }

    throttleConcurrentBuilds {
        maxTotal(1)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github("devtail/jenkins-as-code", "ssh")
                        credentials("deploy-key-shared-library")
                    }

                    branch('master')
                }
            }

            scriptPath('resources/agentManagement/AWSAgentBootstrap.groovy')
        }
    }
}

pipelineJob("Admin/AWSAgentDestroy") {

    parameters {
        stringParam('agentID', '', '') 
    }

    logRotator {
        numToKeep(50)
    }

    throttleConcurrentBuilds {
        maxTotal(1)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github("devtail/jenkins-as-code", "ssh")
                        credentials("deploy-key-shared-library")
                    }

                    branch('master')
                }
            }

            scriptPath('resources/agentManagement/AWSAgentDestroy.groovy')
        }
    }
}


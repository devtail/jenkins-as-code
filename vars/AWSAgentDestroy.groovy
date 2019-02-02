def call(Map config) {
    node('master') {
        stage('Checkout') {
            cleanWs()
            checkout scm
        }

        stage('Terraform') {
            ansiColor('xterm') {
                sh('ln -sfn /var/jenkins_home/agent-bootstrapping-terraform-config/aws-agent-vms.backend.config resources/terraform/aws/agent-vms/')
                sh('ln -sfn /var/jenkins_home/agent-bootstrapping-terraform-config/aws-agent-vms.tfvars resources/terraform/aws/agent-vms/terraform.tfvars')
                sh("cd resources/terraform/ && make destroy-agent-vm AGENT_ID=${config['agentID']}")
            }
        }
    }
}

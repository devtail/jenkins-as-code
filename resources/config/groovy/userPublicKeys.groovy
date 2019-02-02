#! groovy

def getFileContent(filePath) {
    return new File(filePath).text
}

def user = hudson.model.User.get('admin')

def pubKey = new org.jenkinsci.main.modules.cli.auth.ssh.UserPropertyImpl(getFileContent('/var/jenkins_home/jenkins-ssh-keys/ssh-agent-access-key.pub'))
user.addProperty(pubKey)

user.save()

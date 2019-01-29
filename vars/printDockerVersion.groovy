def call(Map config) {
    node('master') {
        stage('Hello') {
            echo('Hello World')
        }
    }

    node('docker') {
        stage('Version') {
            sh('sudo docker version')
        }
    }
}

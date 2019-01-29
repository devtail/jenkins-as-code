projects = [
    [name: "ServiceA"],
    [name: "ServiceB"],
    [name: "ServiceC"]
]

for(project in projects) { 
    folder("${project.name}")

    pipelineJob("${project.name}/build") {
    
        logRotator {
            numToKeep(50)
        }
    
        definition {
            cps {
                sandbox(true)
                script("""@Library('devtail-ci-lib@master') _
printDockerVersion()
                """)
            }
        }
    }
}

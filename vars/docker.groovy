def call() {
    node {
        sh "rm -rf *"
        git branch: 'main', url: "https://github.com/b53-clouddevops/${COMPONENT}.git"

        stage('Docker Build') {
            sh "docker build . "
        }
        // env.APP_TYPE = "maven" 
        // common.lintChecks()
        // env.ARGS="-Dsonar.java.binaries=target/"
        // common.sonarChecks()
        // common.testCases()
        // common.artifacts()
    }
}
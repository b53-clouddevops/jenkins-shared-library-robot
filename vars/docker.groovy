def call() {
    node {
        sh "rm -rf *"
        git branch: 'main', url: "https://github.com/b53-clouddevops/${COMPONENT}.git"

        common.lintChecks()

        stage('Preparing the artifact') {
            if(env.APP_TYPE == "nodejs") {
                sh ''' 
                    npm install
                    zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                '''      
            }
            else if(env.APP_TYPE == "maven") {  
                sh '''
                    mvn clean package
                    mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar 
                    zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
                '''
            }
            else if(env.APP_TYPE == "python") {  
                sh '''
                    zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt
                '''
            }
            else {  
                sh '''
                    echo "Frontend Component Is Executing"
                    zip -r ${COMPONENT}-${TAG_NAME}.zip * 
                    zip -d ${COMPONENT}-${TAG_NAME}.zip Jenkinsfile

                    '''
                }
                    sh "docker build -t 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:latest"
                    sh "docker tag 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:latest 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"
                    sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 834725375088.dkr.ecr.us-east-1.amazonaws.com"
                    sh "docker push 834725375088.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:{TAG_NAME}"
            }
       }
        // env.APP_TYPE = "maven" 
        // common.lintChecks()
        // env.ARGS="-Dsonar.java.binaries=target/"
        // common.sonarChecks()
        // common.testCases()
        // common.artifacts()
    }
}
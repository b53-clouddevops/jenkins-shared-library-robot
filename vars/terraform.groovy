def call() {
        properties([
            parameters([
                choice(choices: 'dev\nprod', description: "Chose the environment", name: "ENV"),
                choice(choices: 'apply\ndestroy', description: "Chose the Action", name: "ACTION"),
            ]),
        ])
        
        node {
           ansiColor('xterm') {
            sh "rm -rf *"
            git branch: 'main', url: "https://github.com/b53-clouddevops/${REPONAME}.git"

            stage('Terraform Init') {
                sh ''' 
                    cd ${TFDIR}
                    terrafile -f env-${ENV}/Terrafile
                    terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars
                '''
            }

            stage('Terraform Plan') {
                sh ''' 
                    cd ${TFDIR}
                    terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                '''
            }

            stage('Terraform Apply ') {
                
                   sh '''
                    cd ${TFDIR}
                    terraform ${ACTION} -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
                '''
                }
            }
        }
    }


    // pipeline {
    //     agent any 
    //     parameters { 
    //         choice(name: 'ENV', choices: ['dev', 'prod'], description: 'Chose the environment') 
    //         choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Chose Apply or Destroy') 
    //     }
    //     options {
    //         ansiColor('xterm')
    //     }
    //     stages {
    //         stage('Terraform Init') {
    //             steps {
    //                 sh "terrafile -f env-${ENV}/Terrafile"
    //                 sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
    //             }
    //         }
    //         stage('Terraform Plan') {
    //             steps {
    //                 sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
    //             }
    //         }
    //         stage('Terraform Apply ') {
    //             steps {
    //                 sh "terraform ${ACTION} -var-file=env-${ENV}/${ENV}.tfvars -auto-approve"
    //                 }
    //             }
    //         }
    //     }
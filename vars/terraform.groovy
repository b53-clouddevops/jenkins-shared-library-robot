def call() {
    node {
        properties([
            parameters([
                choice(choices: ['dev\nprod'], description: "Chose the environment", name: "ENV"),
                choice(choices: ['apply\ndestroy'], description: "Chose the Action", name: "ACTION"),
            ]),
        ])
        ansiColor('xterm') { 

            stage('Terraform Init') {
                sh ''' 
                    ls -ltr
                    terrafile -f env-${ENV}/Terrafile
                    terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars
                '''
            }

            stage('Terraform Plan') {
                sh ''' 
                    terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                '''
            }

            stage('Terraform Apply ') {
                
                   sh '''
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
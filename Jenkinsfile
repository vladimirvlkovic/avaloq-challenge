
pipeline {
    agent any
    tools{
        maven 'Maven 3.6.3'
        jdk 'openjdk-11'
    }
    environment {
        PROJECT_NAME="avaloq-challenge"
    }
    stages {
        stage ('Unit Testing') {
            steps {
                githubNotify description: 'In Progress',  status: 'PENDING', context: 'Unit Testing'
                sh 'mvn -Dtest="**.unit.**" test'
            }
            post {
		always {
			junit 'target/surefire-reports/**/*.xml'
		}
		failure {
                    githubNotify description: 'FAILED, check jenkins console for details',  status: 'FAILURE', context: 'Unit Testing'
                }
                success {
                    githubNotify description: 'OK',  status: 'SUCCESS', context: 'Unit Testing'
                }
            }

        }
        stage ('Integration Tests') {
            when {
            anyOf { branch 'develop'; branch 'master'; branch 'main'; branch 'staging' }
            }
            steps {
                echo 'run only when branch is develop/master/staging'
            }
        }
        stage('SonarQube analysis') {
            steps {
                githubNotify description: 'Performing Analysis',  status: 'PENDING', context: 'SonarQube Analysis'
                script {
                  scannerHome = tool 'sonar'
                  GIT_BRANCH = GIT_BRANCH.replaceAll('/','-')
                  print GIT_BRANCH
                }
                withSonarQubeEnv('sonar') {
                  sh "${scannerHome}/bin/sonar-scanner " +
                      "-Dsonar.projectKey=${PROJECT_NAME}-${GIT_BRANCH} "+
                      "-Dsonar.projectName=${PROJECT_NAME}-${GIT_BRANCH} "+
                      "-Dsonar.sourceEncoding=UTF-8 "+
                      "-Dsonar.sources=src/main "+
                      "-Dsonar.java.binaries=target/classes,target/test-classes "+
                      "-Dsonar.tests=src/test "+
                      "-Dsonar.dynamicAnalysis=reuseReports "+
                      "-Dsonar.junit.reportsPath=target/sunfire-reports "+
                      "-Dsonar.java.coveragePlugin=jacoco "+
                      "-Dsonar.jacoco.reportPath=target "
                }
                timeout(time: 2, unit: 'MINUTES') {
          			script {
            			def qualityGate = waitForQualityGate()
            			if (qualityGate.status != 'OK') {
              				error "Pipeline aborted due to a quality gate failure: ${qualityGate.status}"
            			}
          			}
      			}
      			githubNotify description: 'OK',  status: 'SUCCESS', context: 'SonarQube Analysis'
            }
            post {
                failure {
                    githubNotify description: 'FAILED, check SonarQube for details',  status: 'FAILURE', context: 'SonarQube Analysis'
                }
                success {
                    githubNotify description: 'OK',  status: 'SUCCESS', context: 'SonarQube Analysis'
                }
            }
        }
    	stage ('Deploy to Staging/Test Environment') {
	        when {
		        branch 'staging'
	        }
	        steps {
		        echo 'run only when branch staging'
	        }
	    }
    	stage ('Deploy to Production Environment') {
            when {
                branch 'master'; branch 'main'
            }
            steps {
                echo 'run only when branch master'
            }
	    }
    }

}

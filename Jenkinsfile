
pipeline {
    agent any
    tools{
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
    environment {
        PATH="/var/jenkins_home/miniconda3/bin:$PATH"
    }
    stages {
        stage ('Unit Testing') {
            steps {
                githubNotify description: 'In Progress',  status: 'PENDING', context: 'Unit Testing'
                sh 'mvn -Dmaven.test.failure.ignore=true test '
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
            anyOf { branch 'develop'; branch 'master'; branch 'staging' }
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
                      "-Dsonar.projectKey=testPython-${GIT_BRANCH} " +
					  "-Dsonar.projectName=testPython-${GIT_BRANCH} " +
					  "-Dsonar.projectVersion=1.0 " +
					  "-Dsonar.sources=add.py " +
					  "-Dsonar.language=py " +
					  "-Dsonar.sourceEncoding=UTF-8 " +
					  "-Dsonar.python.xunit.reportPath=nosetests.xml " +
					  "-Dsonar.python.coverage.reportPaths=coverage.xml " +
					  "-Dsonar.python.pylint=/var/jenkins_home/miniconda3/envs/*/bin/pylint " +
					  "-Dsonar.python.pylint_config=.pylintrc " +
					  "-Dsonar.python.pylint.reportPath=/var/jenkins_home/workspace/python-test-pipe/pylint-report.txt"
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
                branch 'master'
            }
            steps {
                echo 'run only when branch master'
            }
	    }
    }

}
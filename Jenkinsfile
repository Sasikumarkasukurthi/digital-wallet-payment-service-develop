@Library('cicd') _
def IMAGE_NAME = "digital-wallet-payment-service"
def REPO_STAGING = "anchore"
def REPO_DEVELOP = "digital-wallet"
def REPO_DELIVER = "digital-wallet"
def REGISTRY_DEVELOP = "dev-docker-registry.tecnotree.com"
def REGISTRY_DELIVER = "registry.tecnotree.com"
def REGISTRY_LOGIN = "dev-docker-registry-login"
def BRANCH_ALPHA = "develop"
def BRANCH_BETA = "release"
def BRANCH_RC = "master"
def executeBIQstages = true
def enableSQgate  = true
def abortPipelineSQgate  = false
def anchoreBAILgate = false
def executeStage = true


// DO NOT MODIFY pipeline below except for the stage 'Build Code' script
def IMAGE_TAG
def IMAGE_OPTIONS
def IMAGE_STAGING
def IMAGE_DEVELOP
def IMAGE_DELIVER


pipeline {
	agent {
		label 'java-11-node'
	}
	options {
		timeout(time: 1, unit: 'HOURS')
	}
	stages {
		stage('Init') {
			steps {
				script {
					echo "initialize build package traceabilty properties"
					IMAGE_TAG = cicd.getImageTag()
					IMAGE_LABEL = '--label release-version='+IMAGE_TAG+' --label branch-name='+GIT_LOCAL_BRANCH+' --label vendor="Tecnotree" .'
					IMAGE_STAGING = "${REPO_STAGING}/${IMAGE_NAME}:${IMAGE_TAG}"
					IMAGE_DEVELOP = "${REPO_DEVELOP}/${IMAGE_NAME}:${IMAGE_TAG}"
					IMAGE_DELIVER = "${REPO_DELIVER}/${IMAGE_NAME}:${IMAGE_TAG}"
				}
			}
		}
		stage('Build Code') {
			steps {
			    script{
				   cicd.build('java', 'digital-wallet', '-U clean install')
				}
			}
		}
		stage('Sonar Project') {
			when { expression { executeBIQstages == true }
				anyOf {
					branch BRANCH_RC; branch BRANCH_BETA;
					tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
					tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP";
				}
			}
			steps {
                script{
				    cicd.setSonarPropsFile()
			    }
		    }
	    }
        stage('Sonar CodeScan') {
			when { expression { executeBIQstages == true } }
			environment {
				scannerHome = tool 'Sonar-scanner'
			}
			steps {
				echo "Sonarqube code analysis"
				withSonarQubeEnv('sonarqube.tecnotree.com') {
					sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectVersion='${IMAGE_TAG}'"
				}
			}
		}
        
		stage('Sonar CodeQualityGate') {
			  when { expression { executeBIQstages == true && enableSQgate == true } }
			  steps {
				echo "verify code analysis on quality gate"
				timeout(time: 10, unit: 'MINUTES') {
					waitForQualityGate abortPipeline: abortPipelineSQgate
				}
			}
		}	
			stage('Build DockerImage') {
			/*when {
			    anyOf { branch BRANCH_RC; branch BRANCH_BETA; branch BRANCH_ALPHA;
						tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
						tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP";
				}
			}*/
			steps {
				script {
				    cicd.docker_build("https://${REGISTRY_DEVELOP}", "${IMAGE_STAGING}", IMAGE_LABEL)
					}
				}
			}
			 stage('Anchore ScanImage') {
			when {
				expression { executeBIQstages == true }
				anyOf { branch BRANCH_RC; branch BRANCH_BETA;
					    tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
		        		    tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP";
				}
			} 
			steps {
				script {
				   cicd.anchore_scan("${REGISTRY_DEVELOP}/${IMAGE_STAGING}", anchoreBAILgate)
				}
			}
		}
		stage('Publish Image') {
			/*when {
				anyOf {	branch BRANCH_RC; branch BRANCH_BETA; branch BRANCH_ALPHA;
						tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
						tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP";
				}
			}*/
			parallel {
				stage('to DevelopRegistry') {
					steps {
						script {
						    cicd.publish_image(REGISTRY_DEVELOP, IMAGE_STAGING, REGISTRY_DEVELOP, IMAGE_DEVELOP, 'development')
							}
						}
					}

				stage('to DeliveryRegistry') {
					when { anyOf { tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
					       tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP" }
					 }
					steps {
						script {
						    cicd.publish_image(REGISTRY_DEVELOP, IMAGE_STAGING, REGISTRY_DELIVER, IMAGE_DELIVER, 'deliver')
							}
						}
					}
				}
		   }
		stage('DeployImage Rancher') {
			when {
				anyOf { branch BRANCH_RC; branch BRANCH_BETA; branch BRANCH_ALPHA;
						tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP";
						tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP";
				}
			}
			environment {
				RANCHER_GLOBAL_USER_API_TOKEN_DEV_RANCHER = credentials('dev-rancher-jenkins-bot')
				RANCHER_GLOBAL_USER_API_TOKEN_RANCHER2 = credentials('rancher2-jenkins-bot')
			}
			parallel {
				stage('1. Alpha') {
					when  { anyOf { branch BRANCH_ALPHA } }
					steps {
							script {
							    cicd.deploy("alpha","${REGISTRY_DEVELOP}/${IMAGE_DEVELOP}")
							}
						}
					}
				stage('2. Beta') {
					when  { anyOf { branch BRANCH_BETA; tag pattern: "^\\d.\\d.\\d(-beta.\\d+)?", comparator: "REGEXP" } }
					steps {
							script {
							    cicd.deploy("beta","${REGISTRY_DEVELOP}/${IMAGE_DEVELOP}")
							}
						}
					}
				stage('3. RC') {
					when  { anyOf { branch BRANCH_RC; tag pattern: "^\\d.\\d.\\d(-rc.\\d+)?", comparator: "REGEXP" } }
					steps {
							script {
								cicd.deploy("rc","${REGISTRY_DEVELOP}/${IMAGE_DEVELOP}")
							}
						}
					}
				}
			}
		}   

		post {
		failure {
			emailext (
				recipientProviders: [[$class: 'DevelopersRecipientProvider']],
				replyTo: 'DL-PE-CICD@tecnotree.com',
				subject: "Jenkins: Failed CICD Pipeline: ${currentBuild.fullDisplayName}",
				body: "Please review console log: ${env.BUILD_URL}"
			)
		}
	}
}

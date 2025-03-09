pipeline {
    agent any
    tools {
	    maven "M3"
	    jdk "java17"
	}

    environment {
        registryCredential = 'e101b1e7-9eed-4665-bedf-672243aad7fb'
        branchName = "master"
        appRegistry = "ghcr.io/nnbros/rtp-gateway"
        rtpRegistry = "https://ghcr.io"
    }
    stages {
        stage('Fetch code'){
            steps {
                checkout scmGit(branches: [[name: "*/$branchName"]], extensions: [], userRemoteConfigs: [[credentialsId: registryCredential, url: 'https://github.com/nnbros/rtp-gateway.git']])
            }
        }
        stage('Build artifact') {
            steps {
                sh 'mvn -B install'
            }
        }
        stage('Build Docker Image') {
            steps {
                withCredentials([string(credentialsId: 'RTP_DEV_1_TOKEN', variable: 'BOT_TOKEN'), file(credentialsId: 'BOT_KEY', variable: 'BOT_KEY')]){
                    script {
                        dockerImage = docker.build( appRegistry + ":$BUILD_NUMBER", "--secret id=BOT_KEY,src=$BOT_KEY --build-arg BOT_TOKEN=$BOT_TOKEN .")
                    }
                }
            }
        }
        stage('Build version increase') {
            environment {
                OLDVERSION = sh ( script: 'echo $(xmlstarlet sel -N p="http://maven.apache.org/POM/4.0.0" -t -v "/p:project/p:version" -n pom.xml)', returnStdout: true).trim()
            }
            steps {
                sh '''VERSIONS=($(echo $OLDVERSION | tr "." "\\n"))
                    MAJOR=${VERSIONS[0]}
                    MINOR=${VERSIONS[1]}
                    BUILD=$((${VERSIONS[2]}+1))
                    NEWVERSION=$(echo "$MAJOR.$MINOR.$BUILD")
                    echo $NEWVERSION
                    xmlstarlet ed -L -N p="http://maven.apache.org/POM/4.0.0" -u "/p:project/p:version" -v $NEWVERSION pom.xml
                '''
            }
        }
        stage('Version push') {
            environment {
                NEWVERSION = sh ( script: 'echo $(xmlstarlet sel -N p="http://maven.apache.org/POM/4.0.0" -t -v "/p:project/p:version" -n pom.xml)', returnStdout: true).trim()
            }
            steps {
                withCredentials([gitUsernamePassword(credentialsId: 'e101b1e7-9eed-4665-bedf-672243aad7fb', gitToolName: 'Default')]) {
                sh 'git commit -a -m "Automatic version change to $NEWVERSION"'
                sh 'git push origin HEAD:$branchName'
                }
            }
        }
        stage('Upload Docker Image') {
            environment {
                VERSION = sh ( script: 'echo $(xmlstarlet sel -N p="http://maven.apache.org/POM/4.0.0" -t -v "/p:project/p:version" -n pom.xml)', returnStdout: true).trim()
            }
            steps{
                script {
                    docker.withRegistry( rtpRegistry, registryCredential ) {
                        dockerImage.push("$VERSION")
                        dockerImage.push("latest")
                    }
                }
            }
        }
        stage('Deploy to rtp-dev') {
            steps {
                script{
                    def remote = [:]
                    remote.name = "rtp-dev"
                    remote.host = "192.168.200.78"
                    remote.port = 2207
                    remote.user = "jenkins"
                    remote.knownHosts = "/var/lib/jenkins/.ssh/known_hosts"
                    remote.identityFile = "/var/lib/jenkins/.ssh/jenkins_rsa"
                    sshCommand remote: remote, command: "docker compose -f /opt/rtp/resources/compose.yml down"
                    sshCommand remote: remote, command: "docker compose -f /opt/rtp/resources/compose.yml up -d"
                }
            }
        }
    }
}

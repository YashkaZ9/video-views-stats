pipeline {

  agent any

  tools {
    maven 'maven-3.9'
    jdk 'jdk-21'
  }

  stages {
    stage("build") {
      steps {
        echo 'building the application...'
        echo 'checking jenkins polling...'
        withMaven(jdk: 'jdk-21', maven: 'maven-3.9') {
          sh 'mvn --version'
          sh 'mvn clean package'
        }
      }
    }

    stage("test") {
      steps {
        echo 'testing the application...'
        sh 'mvn test'
      }
    }

    stage("deploy") {
      steps {
        echo 'deploying the application...'
      }
    }
  }
}

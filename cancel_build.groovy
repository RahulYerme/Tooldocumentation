import groovy.json.*
import hudson.model.*
import jenkins.model.*

def cancelPreviousBuilds() {
    echo env.gitlabSourceBranch
    def jobName = env.JOB_NAME
    def currentBuildNumber = env.BUILD_NUMBER.toInteger()
    def currentJob = Jenkins.instance.getItemByFullName(jobName)
    for (def build : currentJob.builds) {
        def buildBranch = build.getEnvironment()['gitlabSourceBranch']
        if (build.isBuilding() && (build.number.toInteger() < currentBuildNumber) && (buildBranch == gitlabSourceBranch)) {
            echo "Older build ${build.number} Source Branch is ${buildBranch}"
            echo "Older build still queued. Sending kill signal to build number: ${build.number}"
            build.doTerm()
        }
    }
}

return this
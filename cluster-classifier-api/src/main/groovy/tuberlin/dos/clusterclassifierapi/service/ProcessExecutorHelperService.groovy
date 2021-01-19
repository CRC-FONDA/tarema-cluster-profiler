package tuberlin.dos.clusterclassifierapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProcessExecutorHelperService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessExecutorHelperService.class);

    Optional<String> executeProcess(String command, Long waitingTime) {
        logger.info("Execute '$command' with waitingTime $waitingTime")
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = command.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(waitingTime)

        if(!sout.toString().empty) {
            return Optional.of(sout.toString())
        } else {
            logger.error("There was an error executing: '"+ command + "' with waitingTime " + waitingTime)
            return Optional.ofNullable(null)
        }
    }
}

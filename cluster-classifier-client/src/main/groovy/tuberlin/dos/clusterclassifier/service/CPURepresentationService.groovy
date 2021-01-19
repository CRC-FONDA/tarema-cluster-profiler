package tuberlin.dos.clusterclassifier.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifier.entities.CPURepresentation
import tuberlin.dos.clusterclassifier.exception.ProcessErrorException
import tuberlin.dos.clusterclassifier.service.helper.UnitConverter

@Service
class CPURepresentationService {

    private final Logger logger = LoggerFactory.getLogger(CPURepresentationService.class);

    private ProcessExecutorHelperService processExecutorHelperService;

    CPURepresentationService(ProcessExecutorHelperService processExecutorHelperService) {
        this.processExecutorHelperService = processExecutorHelperService
    }

    CPURepresentation createCPURepresentation() {
        logger.info("In createCPURepresentation()")
        def sout = processExecutorHelperService.executeProcess("lscpu", 5000).orElseThrow(() ->new ProcessErrorException())
        def cpu = mapLscpuToCPURepresentation(sout.toString());
        return cpu
    }

    private CPURepresentation mapLscpuToCPURepresentation(String lscpuOutput) {
        logger.info("In mapLscpuToCPURepresentation(): Mapping lscupt process output to CPURepresentation object")

        def lscpuArray = lscpuOutput.split("\n");

        def cpuRepToReturn = new CPURepresentation();
        // TODO einheitliche Einheiten KiB MiB etc...
        for (String line : lscpuArray) {
            if (line.containsIgnoreCase("Architecture:")) {
                cpuRepToReturn.architecture = line.split(":")[1].trim()
            } else if (line.containsIgnoreCase("CPU(s):") && !line.containsIgnoreCase("On-line") && !line.containsIgnoreCase("NUMA")) {
                cpuRepToReturn.cpus = Integer.valueOf(line.split(":")[1].trim())
            } else if (line.containsIgnoreCase("Thread(s) per core:")) {
                cpuRepToReturn.thread_per_core = line.split(":")[1].trim()
            } else if (line.containsIgnoreCase("Core(s) per socket:")) {
                cpuRepToReturn.cores_per_socket = line.split(":")[1].trim()
            } else if (line.containsIgnoreCase("Socket(s):")) {
                cpuRepToReturn.sockets = line.split(":")[1].trim()
            } else if (line.containsIgnoreCase("Model name:")) {
                cpuRepToReturn.model_name = line.split(":")[1].trim()
            } else if (line.containsIgnoreCase("L1d cache:")) {
                cpuRepToReturn.L1d_cache = UnitConverter.convertUnitStringToBytes(line.split(":")[1].trim())
            } else if (line.containsIgnoreCase("L1i cache:")) {
                cpuRepToReturn.L1i_cache = UnitConverter.convertUnitStringToBytes(line.split(":")[1].trim())
            } else if (line.containsIgnoreCase("L2 cache:")) {
                cpuRepToReturn.L2_cache = UnitConverter.convertUnitStringToBytes(line.split(":")[1].trim())
            } else if (line.containsIgnoreCase("L3 cache:")) {
                cpuRepToReturn.L3_cache = UnitConverter.convertUnitStringToBytes(line.split(":")[1].trim())
            } else if (line.containsIgnoreCase("Flags:")) {
                cpuRepToReturn.flags = line.split(":")[1].trim()
            }
        }

        cpuRepToReturn.cpu_speed_st_events_per_second = testCPUST()
        cpuRepToReturn.cpu_speed_mt_events_per_second = testCPUMT(cpuRepToReturn.cpus)

        return cpuRepToReturn;

    }

    private def testCPUST() {
        logger.info("In testCPUST(): Testing CPU single-thread speed")
        def sout = processExecutorHelperService.executeProcess("sysbench cpu --num-threads=1 --cpu-max-prime=20000 run", 20000).orElseThrow(() ->new ProcessErrorException())
        def cpu_st_split = sout.toString().split("\n");

        for (String line : cpu_st_split) {
            if (line.containsIgnoreCase("events per second:")) {
                return Double.valueOf(line.split(":")[1].trim())
            }
        }

    }

    private def testCPUMT(def number_cpus) {
        logger.info("In testCPUMT(): Testing CPU multi-thread speed")
        def sout = processExecutorHelperService.executeProcess('sysbench cpu --num-threads=' + number_cpus + ' --cpu-max-prime=20000 run', 20000).orElseThrow(() ->new ProcessErrorException())

        def cpu_mt_split = sout.toString().split("\n");

        for (String line : cpu_mt_split) {
            if (line.containsIgnoreCase("events per second:")) {
                return Double.valueOf(line.split(":")[1].trim())
            }
        }
    }
}

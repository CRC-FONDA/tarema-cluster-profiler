package tuberlin.dos.clusterclassifierapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifierapi.entities.IORepresentation
import tuberlin.dos.clusterclassifierapi.exception.ProcessErrorException

@Service
class IORepresentationService {


    private final Logger logger = LoggerFactory.getLogger(IORepresentationService.class);

    private ProcessExecutorHelperService processExecutorHelperService;

    IORepresentationService(ProcessExecutorHelperService processExecutorHelperService) {
        this.processExecutorHelperService = processExecutorHelperService
    }

    IORepresentation createIORepresentation() {
        logger.info("In createIORepresentation()")
        def io_seqString = readIOSpecsSequ()
        def io_randString = readIOSpecsRand()

        return mapFioToIORepresentation(io_seqString, io_randString)
    }

    private IORepresentation mapFioToIORepresentation(String io_sequ, String io_rand) {
        logger.info("In mapFioToIORepresentation(): Mapping fio process output to IORepresentation object")
        def io_sequ_split = io_sequ.split("\n")
        def io_rand_split = io_rand.split("\n")

        def ioRepToReturn = new IORepresentation();

        for (String line : io_sequ_split) {
            if (line.contains("read: IOPS")) {
                ioRepToReturn.sequ_read_iops = convertIOPsStringToDouble(line.split(":")[1].split(",")[0].trim())
                ioRepToReturn.sequ_read_bw = convertReadWriteStringToDouble(line.split(":")[1].split(",")[1].trim())
            } else if (line.contains("write: IOPS")) {
                ioRepToReturn.sequ_write_iops = convertIOPsStringToDouble(line.split(":")[1].split(",")[0].trim())
                ioRepToReturn.sequ_write_bw = convertReadWriteStringToDouble(line.split(":")[1].split(",")[1].trim())
            }
        }
        for (String line : io_rand_split) {
            if (line.contains("read: IOPS")) {
                ioRepToReturn.rand_read_iops = convertIOPsStringToDouble(line.split(":")[1].split(",")[0].trim())
                ioRepToReturn.rand_read_bw = convertReadWriteStringToDouble(line.split(":")[1].split(",")[1].trim())
            } else if (line.contains("write: IOPS")) {
                ioRepToReturn.rand_write_iops = convertIOPsStringToDouble(line.split(":")[1].split(",")[0].trim())
                ioRepToReturn.rand_write_bw = convertReadWriteStringToDouble(line.split(":")[1].split(",")[1].trim())
            }
        }

        return ioRepToReturn;

    }

    private String readIOSpecsSequ() {
        logger.info("In readIOSpecsSequ(): Testing IO sequential read-write")
        def sout = processExecutorHelperService.executeProcess("fio --name=seqrw --rw=readwrite --direct=1 --ioengine=libaio --bs=128k --iodepth=64 --size=4G --runtime=20", 30000).orElseThrow(() -> ProcessErrorException)
        return sout.toString()

    }
    // TODO Problem units etc.
    private String readIOSpecsRand() {
        logger.info("In readIOSpecsRand(): Testing IO random read-write")
        def sout = processExecutorHelperService.executeProcess("fio --randrepeat=1 --ioengine=libaio --direct=1 --gtod_reduce=1 --name=randrw --bs=4k --iodepth=64 --size=4G --readwrite=randrw --runtime=20", 30000).orElseThrow(() -> ProcessErrorException)
        return sout.toString()
    }

    private Double convertIOPsStringToDouble(String iops) {
        iops = iops.split("=")[1]
        if (iops.contains("k")) {
            return (Double.valueOf(iops.split("k")[0]) * 1000)
        } else {
            return Double.valueOf(iops)
        }
    }

    private Double convertReadWriteStringToDouble(String rw) {
        def end = rw.indexOf("/s ")
        return Double.valueOf(rw.substring(3, end - 3))
    }
}

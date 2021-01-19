package tuberlin.dos.clusterclassifier.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifier.entities.RAMModule
import tuberlin.dos.clusterclassifier.entities.RAMRepresentation
import tuberlin.dos.clusterclassifier.exception.ProcessErrorException
import tuberlin.dos.clusterclassifier.service.helper.UnitConverter

@Service
class RAMRepresentationService {

    private final Logger logger = LoggerFactory.getLogger(RAMRepresentationService.class);

    private ProcessExecutorHelperService processExecutorHelperService;

    RAMRepresentationService(ProcessExecutorHelperService processExecutorHelperService) {
        this.processExecutorHelperService = processExecutorHelperService
    }

    Optional<RAMRepresentation> createRAMRepresentation() {
        logger.info("In createRAMRepresentation() ")

        RAMRepresentation ramRepresentation;

        def ram_speed = testRAMSpeed();

        try {
            def sout = processExecutorHelperService.executeProcess("dmidecode --type 17",2000 ).orElse("")
            def ram = mapDmidecodeToRAMRepresentation(sout);
            ram.ram_speed = ram_speed
            return Optional.of(ram);
        } catch (Exception e) {
            logger.error("No permissions or no dmidecode command available")
            RAMRepresentation ramRep = new RAMRepresentation()
            ramRep.ram_speed = ram_speed
            return Optional.of(ramRep)
        }


    }

    private RAMRepresentation mapDmidecodeToRAMRepresentation(String dmidecode) {
        logger.info("In mapDmidecodeToRAMRepresentation(): Mapping dmidecode process output to RAMRepresentation object")
        def dmidecodeArray = dmidecode.split("Memory Device");

        def ramToReturn = new RAMRepresentation()

        for (int i = 1; i < dmidecodeArray.length; i++) {
            logger.info("Read the RAM modules")
            def ramModule = extractRAMModulesfromDmidecode(dmidecodeArray[i])
            ramModule.ramRepresentation = ramToReturn;
            ramToReturn.rammodule.add(ramModule)
        }
        return ramToReturn;

    }

    private def testRAMSpeed() {
        logger.info("In testRAMSpeed(): Testing RAM speed")
        def sout = processExecutorHelperService.executeProcess("sysbench --test=memory --memory-block-size=1M --memory-total-size=100G --num-threads=1 run",15000 ).orElseThrow(() -> new ProcessErrorException())
        def cpu_mt_split = sout.toString().split("\n");

        for (String line : cpu_mt_split) {
            if (line.containsIgnoreCase("transferred")) {
                def str = line.split("\\(")[1].trim()
                return UnitConverter.convertUnitStringToGB(str)
            }
        }}


    RAMModule extractRAMModulesfromDmidecode(String dmidecode) {
        logger.info("In mapDmidecodeToRAMRepresentation() - extract RAMModules")
        def dmidecodeArray = dmidecode.split("\n");

        def ramToReturn = new RAMModule()
        // TODO einheitliche Einheiten KiB MiB etc...
        for (String line : dmidecodeArray) {
            println line
            if (line.contains("Size:") && line.split(":")[0].trim().size() < 5) {
                ramToReturn.size = line.split(":")[1].trim()
            } else if (line.contains("Configured Memory Speed:")) {
                ramToReturn.speed = line.split(":")[1].trim()
            } else if (line.contains("Total Width:")) {
                ramToReturn.total_width = line.split(":")[1].trim()
            } else if (line.contains("Data Width:")) {
                ramToReturn.memory_width = line.split(":")[1].trim()
            }
        }


        return ramToReturn;

    }

    private Double convertReadWriteStringToDouble(String rw) {

        def end = rw.indexOf("MiB/s")

        return Double.valueOf(rw.substring(0, end-1))
    }
}

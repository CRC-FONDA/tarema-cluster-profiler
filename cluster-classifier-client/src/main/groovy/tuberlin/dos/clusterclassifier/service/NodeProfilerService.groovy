package tuberlin.dos.clusterclassifier.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifier.entities.NodeRepresentation
import tuberlin.dos.clusterclassifier.exception.ProcessErrorException
import tuberlin.dos.clusterclassifier.repository.NodeRepresentationRepository

import javax.annotation.PostConstruct

@Service
class NodeProfilerService {

    private static final Logger logger = LoggerFactory.getLogger(NodeProfilerService.class)

    private final NodeRepresentationRepository nodeRepresentationRepository;

    private final CPURepresentationService cpuRepresentationService

    private final RAMRepresentationService ramRepresentationService

    private final IORepresentationService ioRepresentationService

    private final ProcessExecutorHelperService processExecutorHelperService

    NodeProfilerService(NodeRepresentationRepository nodeRepresentationRepository, CPURepresentationService cpuRepresentationService,
                        RAMRepresentationService ramRepresentationService, IORepresentationService ioRepresentationService,
                        ProcessExecutorHelperService processExecutorHelperService) {
        this.nodeRepresentationRepository = nodeRepresentationRepository
        this.cpuRepresentationService = cpuRepresentationService
        this.ramRepresentationService = ramRepresentationService
        this.ioRepresentationService = ioRepresentationService
        this.processExecutorHelperService = processExecutorHelperService
    }

    @PostConstruct
    def gatherNodeHardwareInformation() {
        logger.info(" in gatherNodeHardwareInformation() - start collection hardware information")

        def node_ip = getNodeIp().trim();

        Optional<NodeRepresentation> nodeRepresentationOpt = nodeRepresentationRepository.findById(node_ip)

        NodeRepresentation nodeRepresentation

        nodeRepresentationOpt.ifPresentOrElse((nodeRep) -> {
            logger.info("Information for this IP address were already collected. Updating existing information")
            nodeRepresentation = nodeRep
            nodeRepresentation.cpu.updateCPU(cpuRepresentationService.createCPURepresentation())
            nodeRepresentation.ram.updateRAM(ramRepresentationService.createRAMRepresentation().orElse(null))
            nodeRepresentation.io.updateIO(ioRepresentationService.createIORepresentation())

        }, () -> {
            logger.info("Collecting information for a new IP address")
            nodeRepresentation = new NodeRepresentation(cpuRepresentationService.createCPURepresentation(),
                    ramRepresentationService.createRAMRepresentation().orElse(null),
                    ioRepresentationService.createIORepresentation())
            nodeRepresentation.cpu.setNodeRepresentation(nodeRepresentation)
            nodeRepresentation.ram.setNodeRepresentation(nodeRepresentation)
            nodeRepresentation.io.setNodeRepresentation(nodeRepresentation)

        })

        nodeRepresentation.setLastModifiedDate(new Date())
        logger.info("Saving NodeRepresentation to database...")
        def rep = nodeRepresentationRepository.save(nodeRepresentation)
        logger.info("Saved succesfully NodeRepresentation: $rep")
    }

    String getNodeIp() {
        logger.info(" in getNodeIp() - getting external IP address for node")
        return processExecutorHelperService.executeProcess("curl https://ipinfo.io/ip", 15000).orElseThrow(() -> new ProcessErrorException())
    }


}
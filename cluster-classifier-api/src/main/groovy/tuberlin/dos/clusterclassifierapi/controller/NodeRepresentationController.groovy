package tuberlin.dos.clusterclassifierapi.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tuberlin.dos.clusterclassifierapi.entities.NodeRepresentation
import tuberlin.dos.clusterclassifierapi.exception.NodeNotFoundException
import tuberlin.dos.clusterclassifierapi.repository.NodeRepresentationRepository

@RestController
@RequestMapping("/nodes")
class NodeRepresentationController {

    private static final Logger logger = LoggerFactory.getLogger(NodeRepresentationController.class)

    private final NodeRepresentationRepository nodeRepresentationRepository;

    public NodeRepresentationController(NodeRepresentationRepository nodeRepresentationRepository) {
        this.nodeRepresentationRepository = nodeRepresentationRepository;

    }


    @GetMapping
    List<NodeRepresentation> getAllNodeRepresentations() {
        logger.info("getAllNodeRepresentations() got called")
        return nodeRepresentationRepository.findAll();
    }

    @GetMapping("/{ip}")
    NodeRepresentation getNodeRepresentationByIP(@PathVariable String ip) {
        logger.info("getNodeRepresentationByIP() with argument node_ip: " + ip +" got called")
        return nodeRepresentationRepository.findById(ip).orElseThrow(() -> new NodeNotFoundException(ip));

    }

}

package tuberlin.dos.clusterclassifierapi.service

import io.fabric8.kubernetes.api.model.NamespaceBuilder
import io.fabric8.kubernetes.api.model.NodeBuilder
import io.fabric8.kubernetes.api.model.ServiceBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tuberlin.dos.clusterclassifierapi.entities.NodeRepresentation

@Service
class KubernetesNodeLabeller {

    @Value('${kube.enable}')
    private String kube_enable;

    void labelNodes(List<NodeRepresentation> nodeRepresentationList) {

        if (kube_enable.contains("false")) {
            return
        }

        println("Start labelling nodes")

        KubernetesClient client = new DefaultKubernetesClient()
        //client.close()
        io.fabric8.kubernetes.api.model.NodeList nodeList = client.nodes().list();


        //For all Nodes in our DB
        for (NodeRepresentation nodeRep : nodeRepresentationList) {

            // Check all Kub controlled Nodes
            nodeList.getItems().forEach(node -> {

                Map<String, String> labels = new HashMap<>();

                // Is the Kub controlled Node in our DB?
                node.getStatus().getAddresses().forEach(addressr -> {
                    if (addressr.getAddress().contains(nodeRep.getNode_ip())) {
                        // If yes, go through all labels and add them to the kub labels

                        nodeRep.nodeLabels.forEach(label -> {
                            if (label.toString().split("_").size() == 3) {
                                labels.put(label.toString().substring(0, label.toString().lastIndexOf("_")), label.toString().split("_")[label.toString().split("_").size()-1])
                            } else {
                                labels.put(label.toString().split("_")[0], label.toString().split("_")[label.toString().split("_").size()-1])
                            }

                        })

                        client.nodes().withName(node.getMetadata().getName()).edit( s -> new NodeBuilder(s).editMetadata().addToLabels(labels).endMetadata().build())


                    }
                })
            })
        }

        println("Finish labelling nodes")

    }
}

package tuberlin.dos.clusterclassifierapi.repository;


import org.springframework.data.jpa.repository.JpaRepository
import tuberlin.dos.clusterclassifierapi.entities.NodeRepresentation;

interface NodeRepresentationRepository extends JpaRepository<NodeRepresentation, String> {
}

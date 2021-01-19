package tuberlin.dos.clusterclassifier.repository;


import org.springframework.data.jpa.repository.JpaRepository
import tuberlin.dos.clusterclassifier.entities.NodeRepresentation


interface NodeRepresentationRepository extends JpaRepository<NodeRepresentation, String> {
}

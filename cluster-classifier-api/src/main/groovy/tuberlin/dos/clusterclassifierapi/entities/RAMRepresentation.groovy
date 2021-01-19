package tuberlin.dos.clusterclassifierapi.entities

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

@Entity
class RAMRepresentation {

    @Id
    @Column(name = "node_ip")
    private String nodeip;

    @OneToOne
    @MapsId
    @JoinColumn(name = "node_ip")
    @JsonIgnore
    NodeRepresentation nodeRepresentation

    @OneToMany(mappedBy = "ramRepresentation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<RAMModule> rammodule;

    /**
     * unit GB/s
     */
    Double ram_speed

    RAMRepresentation() {
        rammodule = new ArrayList<>()
    }

    def updateRAM(RAMRepresentation ramRep) {
        this.ram_speed = ramRep.ram_speed
        this.rammodule = ramRep.rammodule
    }



    @Override
    public String toString() {
        return "RAMRepresentation{" +
                "nodeip='" + nodeip + '\'' +
                ", rammodule=" + rammodule +
                ", ram_speed='" + ram_speed + '\'' +
                '}';
    }
}

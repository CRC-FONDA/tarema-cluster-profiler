package tuberlin.dos.clusterclassifier.entities

import tuberlin.dos.clusterclassifier.entities.enums.NodeLabel

import javax.persistence.*

@Entity
class NodeRepresentation {

    @Id
    String node_ip

    Date lastModifiedDate

    @OneToOne(mappedBy = "nodeRepresentation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    CPURepresentation cpu

    @OneToOne(mappedBy = "nodeRepresentation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    RAMRepresentation ram;

    @OneToOne(mappedBy = "nodeRepresentation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    IORepresentation io;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = NodeLabel, fetch = FetchType.EAGER)
    @CollectionTable(name = "node_label")
    Set<NodeLabel> nodeLabels;

    public NodeRepresentation(String node_ip) {
        this.node_ip=node_ip
    }

    public NodeRepresentation() {

    }


    NodeRepresentation(CPURepresentation cpu, RAMRepresentation ram, IORepresentation io) {
        this.node_ip = callIpInfo().trim()
        this.cpu = cpu
        this.ram = ram
        this.io = io

    }



    @Override
    public String toString() {
        return "NodeRepresentation{" +
                "node_ip=" + node_ip +
                ", cpu=" + cpu +
                ", ram=" + ram +
                ", io=" + io +
                '}';
    }

    String callIpInfo() {

        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = 'curl https://ipinfo.io/ip'.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(15000)
        println "out> $sout err> $serr"
        return sout
    }
}

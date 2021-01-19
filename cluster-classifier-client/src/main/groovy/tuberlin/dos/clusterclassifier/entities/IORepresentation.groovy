package tuberlin.dos.clusterclassifier.entities


import javax.persistence.*

@Entity
class IORepresentation {

    @Id
    @Column(name = "node_ip")
    private String node_ip;

    @OneToOne
    @MapsId
    @JoinColumn(name = "node_ip")
    NodeRepresentation nodeRepresentation

    /**
     * unit IOPS
     */
    Double sequ_read_iops

    /**
     * unit IOPS
     */
    Double sequ_write_iops

    /**
     * unit MB/s
     */
    Double sequ_read_bw

    /**
     * unit MB/s
     */
    Double sequ_write_bw

    /**
     * unit IOPS
     */
    Double rand_read_iops

    /**
     * unit IOPS
     */
    Double rand_write_iops

    /**
     * unit MB/s
     */
    Double rand_read_bw

    /**
     * unit MB/s
     */
    Double rand_write_bw




    def updateIO(IORepresentation ioRep) {
        this.sequ_read_iops = ioRep.sequ_read_iops
        this.sequ_write_iops = ioRep.sequ_write_iops
        this.sequ_read_bw = ioRep.sequ_read_bw
        this.sequ_write_bw = ioRep.sequ_write_bw
        this.rand_read_iops = ioRep.rand_read_iops
        this.rand_write_iops = ioRep.rand_write_iops
        this.rand_read_bw = ioRep.rand_read_bw
        this.rand_write_bw = ioRep.rand_write_bw
    }


    @Override
    public String toString() {
        return "IORepresentation{" +
                "sequ_read_iops=" + sequ_read_iops +
                ", sequ_write_iops=" + sequ_write_iops +
                ", sequ_read_bw=" + sequ_read_bw +
                ", sequ_write_bw=" + sequ_write_bw +
                ", rand_read_iops=" + rand_read_iops +
                ", rand_write_iops=" + rand_write_iops +
                ", rand_read_bw=" + rand_read_bw +
                ", rand_write_bw=" + rand_write_bw +
                '}';
    }
}

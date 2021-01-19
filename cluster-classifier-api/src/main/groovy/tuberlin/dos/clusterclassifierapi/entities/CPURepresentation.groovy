package tuberlin.dos.clusterclassifierapi.entities

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

import tuberlin.dos.clusterclassifierapi.service.CPURepresentationService

@Entity
class CPURepresentation {

    @Id
    @Column(name = "node_ip")
    private String node_ip;

    @OneToOne
    @MapsId
    @JoinColumn(name = "node_ip")
    @JsonIgnore
    NodeRepresentation nodeRepresentation

    String architecture

    Integer cpus

    String thread_per_core

    String cores_per_socket

    String sockets

    String model_name

    /**
     * In byte
     */
    String L1d_cache

    /**
     * In byte
     */
    String L1i_cache

    /**
     * In byte
     */
    String L2_cache

    /**
     * In byte
     */
    String L3_cache

    @Lob
    String flags

    Double cpu_speed_st_events_per_second

    Double cpu_speed_mt_events_per_second

    CPURepresentation() {
    }

    CPURepresentation(architecture, cpus, thread_per_core, cores_per_socket, sockets, model_name, L1d_cache, L1i_cache, L2_cache, L3_cache, flags) {
        this.architecture = architecture
        this.cpus = cpus
        this.thread_per_core = thread_per_core
        this.cores_per_socket = cores_per_socket
        this.sockets = sockets
        this.model_name = model_name
        this.L1d_cache = L1d_cache
        this.L1i_cache = L1i_cache
        this.L2_cache = L2_cache
        this.L3_cache = L3_cache
        this.flags = flags
    }


    def updateCPU(def cpuRep) {
        this.architecture = cpuRep.architecture
        this.cpus = cpuRep.cpus
        this.thread_per_core = cpuRep.thread_per_core
        this.cores_per_socket = cpuRep.cores_per_socket
        this.sockets = cpuRep.sockets
        this.model_name = cpuRep.model_name
        this.L1d_cache = cpuRep.L1d_cache
        this.L1i_cache = cpuRep.L1i_cache
        this.L2_cache = cpuRep.L2_cache
        this.L3_cache = cpuRep.L3_cache
        this.flags = cpuRep.flags
        this.cpu_speed_st_events_per_second = cpuRep.cpu_speed_st_events_per_second
        this.cpu_speed_mt_events_per_second = cpuRep.cpu_speed_mt_events_per_second

    }


    @Override
    public String toString() {
        return "CPURepresentation{" +
                "architecture=" + architecture +
                ", cpus=" + cpus +
                ", thread_per_core=" + thread_per_core +
                ", cores_per_socket=" + cores_per_socket +
                ", sockets=" + sockets +
                ", model_name=" + model_name +
                ", L1d_cache=" + L1d_cache +
                ", L1i_cache=" + L1i_cache +
                ", L2_cache=" + L2_cache +
                ", L3_cache=" + L3_cache +
                ", flags=" + flags +
                ", cpu_speed_st_events_per_second=" + cpu_speed_st_events_per_second +
                ", cpu_speed_mt_events_per_second=" + cpu_speed_mt_events_per_second +
                '}';
    }
}

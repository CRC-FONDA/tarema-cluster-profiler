package tuberlin.dos.clusterclassifier.entities


import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class RAMModule {

    @Id
    @GeneratedValue
    Long id;

    String size

    String speed

    String total_width

    String memory_width

    @ManyToOne
    RAMRepresentation ramRepresentation

    RAMModule() {
    }

    RAMModule(String size, String speed, String total_width, String memory_width) {
        this.size = size
        this.speed = speed
        this.total_width = total_width
        this.memory_width = memory_width
    }





    @Override
    public String toString() {
        return "Rammodule{" +
                "size=" + size +
                ", speed=" + speed +
                ", total_width=" + total_width +
                ", memory_width=" + memory_width +
                '}';
    }
}

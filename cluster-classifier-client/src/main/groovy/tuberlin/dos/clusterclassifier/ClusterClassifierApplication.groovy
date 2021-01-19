package tuberlin.dos.clusterclassifier

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ClusterClassifierApplication {

    static void main(String[] args) {
        SpringApplication.run(ClusterClassifierApplication, args)
    }

}

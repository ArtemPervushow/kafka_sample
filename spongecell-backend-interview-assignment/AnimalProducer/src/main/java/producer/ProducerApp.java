package producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@SpringBootApplication(scanBasePackages = {"producer/controller/rest", "producer/model", "producer/service"}, exclude = { DataSourceAutoConfiguration.class })
public class ProducerApp {
    public static void main(String[] args) {
            SpringApplication.run(ProducerApp.class, args);
    }
}

package producer.model.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.msgpack.MessagePack;
import org.springframework.stereotype.Component;
import producer.model.AnimalCreateDAO;
import spongecell.animal_farm.model.Animal;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@Component
public class AnimalCreateDAOKafka implements AnimalCreateDAO {

    private Properties propertiesProducer;
    private Properties propertiesConfirm;
    private Producer<String, byte[]> producer;
    private KafkaConsumer<String, String> consumerConfirm;

    public AnimalCreateDAOKafka() {
        propertiesProducer = new Properties();
        propertiesProducer.put("bootstrap.servers", "localhost:9092");
        propertiesProducer.put("acks", "all");
        propertiesProducer.put("retries", 0);
        propertiesProducer.put("batch.size", 16384);
        propertiesProducer.put("linger.ms", 1);
        propertiesProducer.put("buffer.memory", 33554432);
        propertiesProducer.put("key.serializer", StringSerializer.class.getName());
        propertiesProducer.put("value.serializer", ByteArraySerializer.class.getName());

        producer = new KafkaProducer<>(propertiesProducer);

        propertiesConfirm = new Properties();

        propertiesConfirm.put("bootstrap.servers", "localhost:9092");
        propertiesConfirm.put("group.id", "test");
        propertiesConfirm.put("enable.auto.commit", "true");
        propertiesConfirm.put("auto.commit.interval.ms", "1000");
        propertiesConfirm.put("session.timeout.ms", "30000");
        propertiesConfirm.put("key.deserializer", StringDeserializer.class.getName());
        propertiesConfirm.put("value.deserializer", StringDeserializer.class.getName());

        consumerConfirm = new KafkaConsumer<>(propertiesConfirm);
        consumerConfirm.subscribe(Arrays.asList("animal-topic-confirm"));
    }

    @Override
    public Animal persistAnimal(Animal animal) {
        MessagePack messagePack = new MessagePack();
        byte[] bytes = null;

        try {
            bytes = messagePack.write(animal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        producer.send(new ProducerRecord<String, byte[]>("animal-topic", String.valueOf(animal.getId()), bytes),
                new MessageCallback(System.currentTimeMillis(), String.valueOf(animal.getId()), animal.getType()));

        boolean foundConfirm = false;
        while (!foundConfirm) {
            ConsumerRecords<String, String> records = consumerConfirm.poll(100);

            if(records.count() > 0){
                int i = 0;
                i++;
            }

            for (ConsumerRecord<String, String> record : records){
                if(record.key().equals(String.valueOf(animal.getId()))){
                    animal.setId(Integer.parseInt(record.value()));
                    foundConfirm = true;
                    break;
                }
            }
        }

        return animal;
    }

    class MessageCallback implements Callback {
        private long startTime;
        private String key;
        private String message;

        public MessageCallback(long startTime, String key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        public void onCompletion(RecordMetadata metadata, Exception exception) {
            System.out.println("Complete key=" + key + " with message " + message + " with time " + (System.currentTimeMillis() - startTime) );
        }
    }


}

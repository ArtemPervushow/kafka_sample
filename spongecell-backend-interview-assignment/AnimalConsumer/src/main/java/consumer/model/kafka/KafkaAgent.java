package consumer.model.kafka;

import consumer.model.AnimalDAO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.msgpack.MessagePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spongecell.animal_farm.model.Animal;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.msgpack.unpacker.Unpacker;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@Service
public class KafkaAgent {
    private Properties propertiesConsumer;
    private KafkaConsumer<String, byte[]> consumer;

    private Properties propertiesConfirmation;
    private Producer<String, String> producerConfirmation;

    private AnimalDAO animalDAO;

    @Autowired
    public void setAnimalDAO(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    public KafkaAgent() {
        propertiesConsumer = new Properties();

        propertiesConsumer.put("bootstrap.servers", "localhost:9092");
        propertiesConsumer.put("group.id", "test");
        propertiesConsumer.put("enable.auto.commit", "true");
        propertiesConsumer.put("auto.commit.interval.ms", "1000");
        propertiesConsumer.put("session.timeout.ms", "30000");
        propertiesConsumer.put("key.deserializer", StringDeserializer.class.getName());
        propertiesConsumer.put("value.deserializer", ByteArrayDeserializer.class.getName());

        consumer = new KafkaConsumer<>(propertiesConsumer);
        consumer.subscribe(Arrays.asList("animal-topic"));

        propertiesConfirmation = new Properties();
        propertiesConfirmation.put("bootstrap.servers", "localhost:9092");
        propertiesConfirmation.put("acks", "all");
        propertiesConfirmation.put("retries", 0);
        propertiesConfirmation.put("batch.size", 16384);
        propertiesConfirmation.put("linger.ms", 1);
        propertiesConfirmation.put("buffer.memory", 33554432);
        propertiesConfirmation.put("key.serializer", StringSerializer.class.getName());
        propertiesConfirmation.put("value.serializer", StringSerializer.class.getName());

        producerConfirmation = new KafkaProducer<>(propertiesConfirmation);
    }

    @PostConstruct
    public void start(){
        MessagePack msgpack = new MessagePack();
        Thread kafkaThread = new Thread(()->{
            while (true) {
                ConsumerRecords<String, byte[]> records = consumer.poll(100);
                for (ConsumerRecord<String, byte[]> record : records){
                    if(record.value() != null) {
                        ByteArrayInputStream in = new ByteArrayInputStream(record.value());
                        Animal message = null;
                        Unpacker unpacker = msgpack.createUnpacker(in);
                        try {
                            message = unpacker.read(Animal.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String oldKey = String.valueOf(message.getId());
                        Animal animal = animalDAO.insert(message);
                        producerConfirmation.send(new ProducerRecord<String, String>("animal-topic-confirm",
                                                                                            oldKey,
                                                                                            String.valueOf(animal.getId())));
                    }
                }
            }
        });

        kafkaThread.start();
    }


}

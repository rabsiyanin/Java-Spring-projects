package ru.kozlov.Lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.kozlov.Lab.db.DBService;

@SpringBootApplication
public class FileDatabaseApplication {

    @Autowired
    DBService dbService;


    public static void main(String[] args) {
        SpringApplication.run(FileDatabaseApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {

        return args -> {

            //dbService.createDB();
            dbService.importDB();


            //System.out.println(passengerService.findById(2));
            //Passenger passenger = new Passenger(new String[]{"601", "123", "123", "12", "3", "123", "123", "123", "1", "20", "11", "asd"});
            //passengerService.insert(passenger);
        };
    }
}

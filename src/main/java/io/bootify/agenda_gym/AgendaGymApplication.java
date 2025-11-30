package io.bootify.agenda_gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AgendaGymApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AgendaGymApplication.class, args);
    }

}
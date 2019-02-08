package fr.eccad;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EntityScan(basePackages = {"fr.sedoo.eccad2.api.modele","fr.eccad.models"})
@Import({fr.eccad.models.configuration.BeanConfiguration.class})
public class EccadApiApplication implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(EccadApiApplication.class,args);
    }

    @Override
    public void run(String... strings) throws Exception {
    }
}

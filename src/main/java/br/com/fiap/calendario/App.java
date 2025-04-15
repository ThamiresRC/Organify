package br.com.fiap.calendario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
    info = @Info(
        title = "Calendário API",
        version = "v1",
        description = "API para gerenciamento de eventos e usuários",
        contact = @Contact(name = "Thamires Ribeiro e Henrique Maciel")
    )
)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}


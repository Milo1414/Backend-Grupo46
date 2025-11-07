package tpi_grupo46.logistica;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class LogisticaApplication {

	@PostConstruct
	public void init() {
		// Establecer timezone UTC para toda la JVM
		// Esto evita problemas con PostgreSQL y timezones de Windows
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		// Establecer timezone ANTES de que Spring Boot inicie
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(LogisticaApplication.class, args);
	}

}


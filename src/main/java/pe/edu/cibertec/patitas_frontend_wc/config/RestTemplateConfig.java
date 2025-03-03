package pe.edu.cibertec.patitas_frontend_wc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplateAuthenticacion(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8081/autenticacion")
                .setConnectTimeout(Duration.ofSeconds(10))//timeout de establecimiento de conexion
                .setReadTimeout(Duration.ofSeconds(10))//timeout de lectura de la respuesta(cuanto tiempo maximo puede esperar la respuesta)
                .build();
    }
}

package dev.angelcruzl.msvc.users;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class MsvcUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcUsersApplication.class, args);
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Users Microservice API")
                .description("Sample Users Microservice API to handle users and their courses.")
                .version("v1.0.0")
                .contact(new Contact().email("me@angelcruzl.dev").name("Ángel Cruz")))
            .externalDocs(new ExternalDocumentation()
                .description("Courses Microservice Documentation")
                .url("https://msvc-courses.angelcruzl.dev/swagger-ui/index.html"));
    }

}

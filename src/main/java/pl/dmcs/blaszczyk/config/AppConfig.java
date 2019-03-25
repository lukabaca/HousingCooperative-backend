package pl.dmcs.blaszczyk.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"pl.dmcs.blaszczyk"})
@ComponentScan(basePackages = "pl.dmcs.blaszczyk")
public class AppConfig {

}
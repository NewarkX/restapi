package spring.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableCaching
@SpringBootApplication
public class CursoSpringRestapiApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CursoSpringRestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}

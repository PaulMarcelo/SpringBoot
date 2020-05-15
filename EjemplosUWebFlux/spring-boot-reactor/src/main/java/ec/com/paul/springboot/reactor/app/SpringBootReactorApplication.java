package ec.com.paul.springboot.reactor.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	public void run(String... args) throws Exception {
		Flux<String> nombre = Flux.just("Paul", "Marcelo", "Yaguachi", "Barahona").doOnNext(System.out::println);
		nombre.subscribe();

	}

}

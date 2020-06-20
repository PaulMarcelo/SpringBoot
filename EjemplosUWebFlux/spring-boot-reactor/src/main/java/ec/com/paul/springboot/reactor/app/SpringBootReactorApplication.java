package ec.com.paul.springboot.reactor.app;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

// TODO: Auto-generated Javadoc
/**
 * The Class SpringBootReactorApplication.
 */
@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	public void run(String... args) throws Exception {
		Flux<String> nombre = Flux.just("Andres", "Pedro", "Jose", "Diego", "Juan")
				// .doOnNext(System.out::println);
				.doOnNext(e -> {
					if (e.isEmpty()) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					System.out.println();

				});
		// nombre.subscribe(log::info);
		nombre.subscribe(e -> log.info(e), error -> log.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				log.info("Ha finalizado  la ejecución del observable con éxito.");
			}
		});

	}

}

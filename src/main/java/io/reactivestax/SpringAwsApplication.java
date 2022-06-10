package io.reactivestax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class SpringAwsApplication {

	@GetMapping
	public String applicationStatus() {
		return "Application is up and running";
	}

	@GetMapping("/{name}")
	public String welcome(@PathVariable String name) {
		return "Hi" + name+ "Welcome to java techie AWS ECS Example";

	}

	public static void main(String[] args) {
		SpringApplication.run(SpringAwsApplication.class, args);
	}

}

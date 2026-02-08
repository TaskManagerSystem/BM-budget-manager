package ua.tms.budgetmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(value = {
		"org.springframework.security.crypto.password.PasswordEncoder"
})

@SpringBootApplication
public class BudgetmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetmanagerApplication.class, args);
	}

}

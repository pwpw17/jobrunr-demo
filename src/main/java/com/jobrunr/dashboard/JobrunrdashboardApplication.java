package com.jobrunr.dashboard;

import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;
import com.jobrunr.dashboard.model.Employee;
import com.jobrunr.dashboard.repository.EmployeeRepository;
import com.jobrunr.dashboard.service.JobService;

@SpringBootApplication
public class JobrunrdashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobrunrdashboardApplication.class, args);

//		BackgroundJob.enqueue(() -> System.out.println("Hello, world!"));
	}

	@Bean
	public CommandLineRunner demo(EmployeeRepository repository) {
		final Faker faker = new Faker();
		return (args) -> {
			for (int i = 0; i < 20; i++) {
				repository.save(new Employee(null, faker.name().firstName(), faker.name().lastName(),
						faker.internet().emailAddress(),null));
			}

//			BackgroundJob.scheduleRecurringly("generate-and-send-salary-slip",
//					SalarySlipService::generateAndSendSalarySlipToAllEmployees, Cron.weekly(DayOfWeek.SUNDAY, 22));

//			BackgroundJob.scheduleRecurringly("generate-employee", JobService::exampleJob,
//					Cron.weekly(DayOfWeek.SUNDAY, 22)
//					);

			BackgroundJob.scheduleRecurrently("generate-employee", "0 0 19 * * *",
					JobService::exampleJob);

//			BackgroundJob.scheduleRecurrently("some-id", "0 12 * */2",
//					  () -> System.out.println("Powerful!"));
			
			Thread.currentThread().join();
		};
	}

}

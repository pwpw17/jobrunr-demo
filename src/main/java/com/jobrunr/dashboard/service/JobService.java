package com.jobrunr.dashboard.service;

import java.util.stream.Stream;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jobrunr.dashboard.model.Employee;
import com.jobrunr.dashboard.repository.EmployeeRepository;

import static java.lang.String.format;
import static java.time.LocalDate.now;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class JobService {

	private static final Path salarySlipTemplatePath = Paths.get("src/main/resources/templates/salary-slip-template.docx");
	private static final String pdfSaveLocation = "D:\\tempSlip";

	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	DocumentGenerationService documentGenerationService;

	public JobService() {
		// TODO Auto-generated constructor stub
	}

	@Transactional(readOnly = true)
	@Job(name = "Generate all employees")
	public void exampleJob() {
		Stream<Long> allEmployees = employeeRepository.getAllEmployeeIds();
//		BackgroundJob.enqueue(() -> System.out.println("hello world"));
		BackgroundJob.<JobService, Long>enqueue(allEmployees,
				(jobService, employeeId) -> jobService.printAll(employeeId));
	}

	@Job(name = "Generate employee %0")
	public void printAll(Long employeeId) throws Exception {
		Employee employee = getEmployee(employeeId);
//		System.out.println("ID : " + employee.getId() + " first name : " + employee.getFirstName() + " last name : "
//				+ employee.getFirstName());
		System.out.println("generate pdf "+employee.getId());
		generateSalarySlipDocumentUsingTemplate(employee);
	}

	private Path generateSalarySlipDocumentUsingTemplate(Employee employee) throws Exception {
		Path salarySlipPath = Paths.get(pdfSaveLocation, String.valueOf(now().getYear()),
				format("salary-slip-employee-%d.pdf", employee.getId()));
		documentGenerationService.generateDocument(salarySlipTemplatePath, salarySlipPath, employee);
		return salarySlipPath;
	}

	private Employee getEmployee(Long employeeId) {
		return employeeRepository.findById(employeeId).orElseThrow(
				() -> new IllegalArgumentException(format("Employee with id '%d' does not exist", employeeId)));
	}

}

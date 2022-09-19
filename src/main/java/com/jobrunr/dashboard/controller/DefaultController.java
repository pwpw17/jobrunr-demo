package com.jobrunr.dashboard.controller;

import static java.lang.String.format;
import static java.time.LocalDate.now;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wickedsource.docxstamper.replace.typeresolver.image.Image;

import com.jobrunr.dashboard.model.Employee;
import com.jobrunr.dashboard.repository.EmployeeRepository;
import com.jobrunr.dashboard.service.DocumentGenerationService;

@RestController
public class DefaultController {

//	private static final Path salarySlipTemplatePath = Paths.get("D:\\tempSlip\\salary-slip-template.docx");

	private static final Path salarySlipTemplatePath = Paths.get("src/main/resources/templates/salary-slip-template.docx");
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	DocumentGenerationService documentGenerationService;

	@GetMapping("/generatePdf")
	public String test() {
//		Stream<Long> allEmployees = employeeRepository.getAllEmployeeIds();
		Employee employee = employeeRepository.findById(1L).get();
		
		
		Path salarySlipPath = Paths.get("D:\\tempSlip", String.valueOf(now().getYear()),
				format("salary-slip-employee-%d.pdf", employee.getId()));
		try {
			File imageFile = new File("D:\\sign1.png");
			InputStream in = new FileInputStream(imageFile);
			Image image = new Image(in);
			employee.setImage(image);
			documentGenerationService.generateDocument(salarySlipTemplatePath, salarySlipPath, employee);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK";
	}
}

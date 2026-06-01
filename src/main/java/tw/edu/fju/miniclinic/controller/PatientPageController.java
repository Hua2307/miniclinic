package tw.edu.fju.miniclinic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

@Controller
public class PatientPageController {

	@Autowired
	private PatientRepository patientRepo;

	@GetMapping("/patients")
	public String listPatients(
			@RequestParam(required = false) String name,
			Model model) {

		List<Patient> patients;
		if (name == null || name.isBlank()) {
			patients = patientRepo.findAll();
		} else {
			patients = patientRepo.findByName(name);
		}

		model.addAttribute("patients", patients);
		model.addAttribute("selectedName", name);

		return "patients";
	}
}

package tw.edu.fju.miniclinic.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

@RestController
public class PatientApiController {
	@Autowired
	private PatientRepository patientRepo;

	@GetMapping("/api/patients")
	public List<Patient> getPatients(@RequestParam(required = false) String name) {
		if (name == null || name.isBlank()) {
			return patientRepo.findAll();
		}
		return patientRepo.findByName(name);
	}

	@GetMapping("/api/patients/{chartNo}")
	public ResponseEntity<Patient> getPatient(@PathVariable @NonNull String chartNo) {
		Optional<Patient> patient = patientRepo.findById(chartNo);
		return patient
			.map(p -> ResponseEntity.ok(p))
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/api/patients")
	public ResponseEntity<Patient> createPatient(@RequestBody @NonNull Patient patient) {
		Patient saved = patientRepo.save(patient);
		return ResponseEntity.status(201).body(saved);
	}

	@PutMapping("/api/patients/{chartNo}")
	public ResponseEntity<Patient> updatePatient(
			@PathVariable @NonNull String chartNo,
			@RequestBody @NonNull Patient updated) {

		return patientRepo.findById(chartNo)
			.map(existing -> {
				existing.setName(updated.getName());
				existing.setGender(updated.getGender());
				existing.setBirthDate(updated.getBirthDate());
				existing.setPhone(updated.getPhone());
				return ResponseEntity.ok(patientRepo.save(existing));
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/api/patients/{chartNo}")
	public ResponseEntity<Void> deletePatient(@PathVariable @NonNull String chartNo) {
		if (!patientRepo.existsById(chartNo)) {
			return ResponseEntity.notFound().build();
		}
		patientRepo.deleteById(chartNo);
		return ResponseEntity.noContent().build();
	}
}

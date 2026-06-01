package tw.edu.fju.miniclinic.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByName(String name);
}
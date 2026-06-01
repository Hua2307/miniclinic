package tw.edu.fju.miniclinic.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByApptDate(LocalDate apptDate);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    long countByApptDateBetween(LocalDate from, LocalDate to);
    List<Appointment> findByDoctorAndApptDate(Doctor doctor, LocalDate apptDate);  // 新加入

    @Query("SELECT d.department AS department, COUNT(a) AS count " +
        "FROM Appointment a JOIN a.doctor d " +
        "GROUP BY d.department ORDER BY d.department")
    List<DepartmentCount> countAppointmentsByDepartment();

    interface DepartmentCount {
        String getDepartment();
        long getCount();
    }
}
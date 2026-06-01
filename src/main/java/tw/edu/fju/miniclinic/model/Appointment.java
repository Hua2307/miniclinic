package tw.edu.fju.miniclinic.model;

import java.sql.Types;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appt_id")
    private Long apptId;

    // 多個 Appointment 對應一個 Patient
    @ManyToOne
    @JoinColumn(name = "chart_no", nullable = false)
    private Patient patient;

    // 多個 Appointment 對應一個 Doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @JdbcTypeCode(Types.VARCHAR)
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "appt_date", nullable = false, columnDefinition = "TEXT")
    private LocalDate apptDate;

    @Column(name = "time_slot", length = 20, nullable = false)
    private String timeSlot;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "BOOKED";

    public Appointment() {}

    public Long getApptId() { return apptId; }
    public void setApptId(Long apptId) { this.apptId = apptId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDate getApptDate() { return apptDate; }
    public void setApptDate(LocalDate apptDate) { this.apptDate = apptDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
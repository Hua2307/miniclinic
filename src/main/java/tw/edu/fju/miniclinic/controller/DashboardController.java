package tw.edu.fju.miniclinic.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

@Controller
public class DashboardController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        // Session 裡的 doctorId 查不到對應醫師（資料被刪除等異常情況）
        if (doctor == null) {
            session.invalidate();
            return "redirect:/login";
        }

        LocalDate today = LocalDate.now();
        List<Appointment> myAppointments = appointmentRepo.findByDoctorAndApptDate(doctor, today);

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", myAppointments);
        model.addAttribute("today", today);

        // add stats
        long totalDoctors = doctorRepo.count();
        long totalPatients = patientRepo.count();
        long totalAppointments = appointmentRepo.count();

        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalAppointments", totalAppointments);

        List<AppointmentRepository.StatusCount> list = appointmentRepo.countGroupByStatus();
        Map<String, Long> byStatus = new HashMap<>();
        byStatus.put("BOOKED", 0L);
        byStatus.put("COMPLETED", 0L);
        byStatus.put("CANCELLED", 0L);
        for (AppointmentRepository.StatusCount s : list) {
            if (s.getStatus() != null) {
                byStatus.put(s.getStatus(), s.getCount());
            }
        }
        model.addAttribute("byStatus", byStatus);

        return "dashboard";
    }
}
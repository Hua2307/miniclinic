package tw.edu.fju.miniclinic.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import tw.edu.fju.miniclinic.model.StatsDto;

@RestController
@RequestMapping("/api")
public class StatsApiController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping(value = "/stats", produces = "application/json")
    public StatsDto stats() {
        StatsDto dto = new StatsDto();

        long totalDoctors = doctorRepo.count();
        long totalPatients = patientRepo.count();
        long totalAppointments = appointmentRepo.count();

        dto.setTotalDoctors(totalDoctors);
        dto.setTotalPatients(totalPatients);
        dto.setTotalAppointments(totalAppointments);
        dto.setSource("mini-clinic");

        List<AppointmentRepository.StatusCount> list = appointmentRepo.countGroupByStatus();
        StatsDto.ByStatusDto byStatus = new StatsDto.ByStatusDto();
        // default zeros
        byStatus.setBOOKED(0L);
        byStatus.setCOMPLETED(0L);
        byStatus.setCANCELLED(0L);

        for (AppointmentRepository.StatusCount s : list) {
            if (s.getStatus() == null) continue;
            switch (s.getStatus()) {
                case "BOOKED" -> byStatus.setBOOKED(s.getCount());
                case "COMPLETED" -> byStatus.setCOMPLETED(s.getCount());
                case "CANCELLED" -> byStatus.setCANCELLED(s.getCount());
                default -> {
                }
            }
        }

        dto.setByStatus(byStatus);
        dto.setGeneratedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return dto;
    }
}

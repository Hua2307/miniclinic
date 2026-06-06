package tw.edu.fju.miniclinic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsDto {
    private long totalDoctors;
    private long totalPatients;
    private long totalAppointments;
    private ByStatusDto byStatus;
    private String source;
    private String generatedAt;

    public StatsDto() {}

    public long getTotalDoctors() { return totalDoctors; }
    public void setTotalDoctors(long totalDoctors) { this.totalDoctors = totalDoctors; }

    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }

    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }

    public ByStatusDto getByStatus() { return byStatus; }
    public void setByStatus(ByStatusDto byStatus) { this.byStatus = byStatus; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }

    public static class ByStatusDto {
        @JsonProperty("BOOKED")
        private long BOOKED;
        @JsonProperty("COMPLETED")
        private long COMPLETED;
        @JsonProperty("CANCELLED")
        private long CANCELLED;

        public ByStatusDto() {}

        @com.fasterxml.jackson.annotation.JsonIgnore
        public long getBOOKED() { return BOOKED; }
        public void setBOOKED(long BOOKED) { this.BOOKED = BOOKED; }

        @com.fasterxml.jackson.annotation.JsonIgnore
        public long getCOMPLETED() { return COMPLETED; }
        public void setCOMPLETED(long COMPLETED) { this.COMPLETED = COMPLETED; }

        @com.fasterxml.jackson.annotation.JsonIgnore
        public long getCANCELLED() { return CANCELLED; }
        public void setCANCELLED(long CANCELLED) { this.CANCELLED = CANCELLED; }
    }
}

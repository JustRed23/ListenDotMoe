package dev.JustRed23.ListenDotMoe.Music.details;

public class Duration {

    private int durationHours;
    private int durationMinutes;
    private int durationSeconds;

    private String startDate;
    private String startTime;

    public Duration(int duration, String startDate, String startTime) {
        durationSeconds = duration % 60;
        durationMinutes = (duration / 60) % 60;
        durationHours = (duration / 60) / 60;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String toString() {
        return "Duration{" +
                "durationHours=" + durationHours +
                ", durationMinutes=" + durationMinutes +
                ", durationSeconds=" + durationSeconds +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}

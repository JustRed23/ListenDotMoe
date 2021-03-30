package dev.JustRed23.ListenDotMoe.Music.details;

public class Duration {

    private final int durationHours;
    private final int durationMinutes;
    private final int durationSeconds;

    private final String startDate;
    private final String startTime;

    public Duration(int duration, String startDate, String startTime) {
        this.durationSeconds = duration % 60;
        this.durationMinutes = (duration / 60) % 60;
        this.durationHours = (duration / 60) / 60;
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

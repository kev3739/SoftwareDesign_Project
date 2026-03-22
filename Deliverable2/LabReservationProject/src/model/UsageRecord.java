package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UsageRecord {
    private String recordId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private String operationStatus;

    public UsageRecord(String recordId, LocalDateTime startTime, LocalDateTime endTime, String operationStatus) {
        this.recordId = recordId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.operationStatus = operationStatus;
        this.durationMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
    }

    public String getRecordId() {
        return recordId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getOperationStatus() {
        return operationStatus;
    }
}

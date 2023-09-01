package cs211.project.services;

import cs211.project.models.collections.EventList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventHardCode {
    public EventList readData(){
        EventList eventList = new EventList();

        LocalDateTime startDate = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime startTime = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalDateTime timeTeam = LocalDateTime.of(2023, 5, 5, 9, 0);
        LocalDateTime timeParticipant = LocalDateTime.of(2023, 5, 5, 9, 30);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedStartDate = startDate.format(dateFormatter);
        String formattedEndDate = endDate.format(dateFormatter);
        String formattedStartTime = startTime.format(timeFormatter);
        String formattedEndTime = endTime.format(timeFormatter);
        String formattedTimeTeam = timeTeam.format(timeFormatter);
        String formattedTimeParticipant = timeParticipant.format(timeFormatter);

        eventList.addNewEvent("Fes", formattedStartDate, formattedEndDate, formattedStartTime,
                formattedEndTime, 20, 10, "abc", formattedTimeTeam, formattedTimeParticipant);

        return eventList;
    }
}

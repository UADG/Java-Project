package cs211.project.services;

import cs211.project.models.collections.EventList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventHardCode {
    public EventList readData(){
        EventList eventList = new EventList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = "2023-09-01";
        String formattedEndDate = "2023-09-01";

        LocalDate localDate = LocalDate.parse(formattedStartDate, formatter);
        LocalDate localDate1 = LocalDate.parse(formattedEndDate, formatter);

        eventList.addNewEvent("Fes", localDate, localDate1, "00:00",
                "00:00", 20, 10, "abc", "00:00", "00:00", "ai");

        return eventList;
    }
}

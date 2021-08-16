package com.demo.fabrick.utility;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class LocalDateUtility {

    public LocalDate getFirstNotWorkingDay() {
        LocalDate firstNotWorkingDay = LocalDate.now();
        while (firstNotWorkingDay.getDayOfWeek() != DayOfWeek.SATURDAY
                && firstNotWorkingDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
            firstNotWorkingDay = firstNotWorkingDay.plusDays(1);
        }
        return firstNotWorkingDay;
    }

    public LocalDate getFutureWorkingDay(int daysToAdd) {
        LocalDate firstNotWorkingDay = LocalDate.now();
        int daysAdded = 0;

        while (daysAdded < daysToAdd){
            firstNotWorkingDay = firstNotWorkingDay.plusDays(1);
            if (!(firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SATURDAY
                    || firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SUNDAY))
                daysAdded++;
        }
        return firstNotWorkingDay;
    }

    public LocalDate getFirstPastWorkingDay() {
        LocalDate firstNotWorkingDay = LocalDate.now().minusDays(1);
        while (firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SATURDAY
                || firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            firstNotWorkingDay = firstNotWorkingDay.minusDays(1);
        }
        return firstNotWorkingDay;
    }

    public LocalDate getFirstWorkingDay() {
        LocalDate firstNotWorkingDay = LocalDate.now();
        while (firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SATURDAY
                || firstNotWorkingDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            firstNotWorkingDay = firstNotWorkingDay.plusDays(1);
        }
        return firstNotWorkingDay;
    }

}

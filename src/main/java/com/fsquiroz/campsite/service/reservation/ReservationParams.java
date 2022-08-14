package com.fsquiroz.campsite.service.reservation;

public record ReservationParams(int minStayDays, int maxStayDays, int maxDefaultDaysToSearch,
                                int minAheadArrivalReservationDays, int maxAheadArrivalReservationDays) {
}

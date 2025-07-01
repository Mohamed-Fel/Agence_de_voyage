package com.example.demo.services;

import java.util.List;
import java.util.Map;

public interface DashbordService {
	Long getTotalBookingsToday();
	Map<String, Long> getReservationCountByStatus();
	Map<String, Long> getReservationCountByPaymentMethod();
    List<Object[]> getTop3ProduitsReservedInMonth(int year, int month);
    boolean checkIfReservationsExistInMonth(int year, int month);
    List<Map<String, Object>> getTop3Destinations();
	


}

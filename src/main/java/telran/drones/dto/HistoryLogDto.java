package telran.drones.dto;

import java.time.LocalDateTime;

import telran.drones.entities.Drone;
import telran.drones.entities.Medication;

public record HistoryLogDto (
		LocalDateTime timestamp,
		int butteryPersent, 
		Drone drone, 
		Medication medication) {

}

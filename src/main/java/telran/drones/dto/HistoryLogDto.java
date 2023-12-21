package telran.drones.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import telran.drones.entities.Drone;
import telran.drones.entities.Medication;

public record HistoryLogDto (
		LocalDateTime timestamp,
		State state,
		@Max(100)
		int butteryPersent, 
		Drone drone, 
		Medication medication) {

}

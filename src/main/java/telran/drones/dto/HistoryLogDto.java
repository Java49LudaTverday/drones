package telran.drones.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.drones.entities.Drone;
import telran.drones.entities.Medication;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryLogDto {
		LocalDateTime timestamp;
		State state;
		@Max(100)
		byte butteryPersent; 
		Drone drone; 
		Medication medication;

}

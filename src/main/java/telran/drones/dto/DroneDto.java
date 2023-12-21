package telran.drones.dto;

import jakarta.validation.constraints.*;

public record DroneDto(
		@Size(max = 100) String serialNumber, 
		@NotNull ModelType model, 
		@Max(500) int weight,
		@Max(100) @NotNull byte butteryLevel, 
		@NotNull State state) {

}

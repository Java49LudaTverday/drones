package telran.drones.dto;

import jakarta.validation.constraints.*;

public record DroneDto(
		@Size(max = 100) String serialNumber, 
		@NotNull ModelDrone model, 
		@Max(500) int weight,
		@Max(100) @NotNull int butteryCapacity, 
		@NotNull StateDrone state) {

}

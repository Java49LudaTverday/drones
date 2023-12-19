package telran.drones.dto;

import jakarta.validation.constraints.*;

public record MedicationDto(
		@Pattern(regexp = "[\\w-]+") @NotNull String name, 
		@NotNull int weight,
		@Pattern(regexp = "[A-Z0-9_]+") String code) {
}

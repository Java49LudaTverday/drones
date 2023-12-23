package telran.drones.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static telran.drones.api.ConstraintConstant.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicationDto {
	@Pattern(regexp = MEDICATION_NAME_REGEXP)
	@NotEmpty(message = MISSING_MEDICATION_CODE)
	String code;
	@Pattern(regexp = MEDICATION_NAME_REGEXP)
	@NotEmpty(message = MISSING_MEDICATION_CODE)
	String name;
	@Max(value = MAX_WEIGHT, message = MAX_WEIGHT_VIOLATION)
	@Positive
	int weight;

}

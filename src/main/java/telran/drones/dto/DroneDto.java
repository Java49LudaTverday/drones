package telran.drones.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static telran.drones.api.ConstraintConstant.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DroneDto{
		@Size(max = MAX_DRONE_NUMBER_SIZE, message=DRONE_NUMBER_WRONG_LENGTH) 
		@NotEmpty(message=MISSING_DRONE_NUMBER)
		String serialNumber; 
		
		@NotNull(message=MISSING_MODEL) 
		ModelType model; 
		
		@NotNull(message=MISSING_WEIGHT_LIMIT)
		@Positive
		@Max(value=MAX_WEIGHT, message=MAX_WEIGHT_VIOLATION) 
		Integer weight;
		
		@Max(value=MAX_BATTERY_CAPACITY, message = MAX_PERCENTAGE_VIOLATION) 
		@NotNull(message = MISSING_BATTERY_CAPACITY) 
		@Positive 
		Byte batteryLevel; 
		
		@NotNull(message = MISSING_STATE) 
		State state;

}

package telran.drones.api;

public interface ServiceExceptionMessages {
	// defining exception`s messages
	String DRONE_ALREADY_EXISTS = "Drone already exists";
	String DRONE_NOT_FOUND = "Drone Not Found";
	String MEDICATION_NOT_FOUND = "Medication Not Found";
	String WEIGHT_LIMIT_VIOLATION = "Dron's weight limit less than medication weight";
	String NOT_IDLE_STATE = "Loading may be done only in IDLE state";
	String LOW_BATTERY_LEVEL = "Low Battery Level";
	
}

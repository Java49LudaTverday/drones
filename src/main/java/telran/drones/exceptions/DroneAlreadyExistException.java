package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

public class DroneAlreadyExistException extends IllegalStateException {
	private static final long serialVersionUID = 1L;
public DroneAlreadyExistException () {
	super(ServiceExceptionMessages.DRONE_ALREADY_EXISTS);
}
	
	

}

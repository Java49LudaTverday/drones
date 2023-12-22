package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class IllegalStateDroneException extends IllegalStateException {

	public IllegalStateDroneException () {
		super(ServiceExceptionMessages.NOT_IDLE_STATE);
	}

}

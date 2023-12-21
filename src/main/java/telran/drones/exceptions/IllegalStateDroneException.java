package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

public class IllegalStateDroneException extends IllegalStateException {
	private static final long serialVersionUID = 1L;
	public IllegalStateDroneException () {
		super(ServiceExceptionMessages.NOT_IDLE_STATE);
	}

}

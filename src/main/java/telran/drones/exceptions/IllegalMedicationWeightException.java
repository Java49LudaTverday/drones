package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

public class IllegalMedicationWeightException extends IllegalStateException {

	private static final long serialVersionUID = 1L;
	public IllegalMedicationWeightException() {
		super(ServiceExceptionMessages.WEIGHT_LIMIT_VIOLATION);
	}

}

package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;
import telran.exceptions.NotFoundException;

public class MedicationNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 1L;

	public MedicationNotFoundException() {
		super(ServiceExceptionMessages.MEDICATION_NOT_FOUND);
		
	}

}

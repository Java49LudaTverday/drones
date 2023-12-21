package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

public class LowBatteryLevelException extends IllegalStateException{
	private static final long serialVersionUID = 1L;
	public LowBatteryLevelException () {
		super(ServiceExceptionMessages.LOW_BATTERY_LEVEL);
	}
}

package telran.drones.exceptions;

import telran.drones.api.ServiceExceptionMessages;

@SuppressWarnings("serial")
public class LowBatteryLevelException extends IllegalStateException{
	
	public LowBatteryLevelException () {
		super(ServiceExceptionMessages.LOW_BATTERY_LEVEL);
	}
}

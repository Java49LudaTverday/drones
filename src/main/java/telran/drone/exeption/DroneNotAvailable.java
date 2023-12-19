package telran.drone.exeption;

public class DroneNotAvailable extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DroneNotAvailable (String message) {
		super(message);
	}

}

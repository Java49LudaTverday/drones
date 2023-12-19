package telran.drones.exeption;

public class DroneOverWeight extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DroneOverWeight (String message) {
		super(message);
	}
}

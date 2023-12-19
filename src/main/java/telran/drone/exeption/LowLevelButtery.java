package telran.drone.exeption;

public class LowLevelButtery extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public LowLevelButtery (String message) {
		super(message);
	}
}

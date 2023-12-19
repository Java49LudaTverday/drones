package telran.drones.dto;

public record DroneDto(int id, ModelDrone model, int weight, 
		int buttaryCapacity, StateDrone state) {

}

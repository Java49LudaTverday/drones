package telran.drones.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.dto.State;
import telran.drones.entities.Drone;

public interface DroneRepo extends JpaRepository<Drone, String> {
	
	 Drone findByWeightAndStateAndButteryLevel(int weight, State state, int butteryLevel );
}

package telran.drones.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.dto.State;
import telran.drones.entities.Drone;

public interface DroneRepo extends JpaRepository<Drone, String> {
	
	 List<Drone> findByStateAndBatteryLevelGreaterThan(State state, int batteryLevel);
}

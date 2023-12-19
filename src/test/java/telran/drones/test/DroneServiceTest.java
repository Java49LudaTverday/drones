package telran.drones.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.drones.dto.DroneDto;
import telran.drones.dto.ModelDrone;
import telran.drones.dto.StateDrone;
import telran.drones.entities.Drone;
import telran.drones.repo.DroneRepo;
import telran.drones.service.DroneService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DroneServiceTest {
	@Autowired
	DroneService service;
	@Autowired
	DroneRepo droneRepo;
	
	@Test
	void registeringDroneTest() {
		DroneDto newDrone = new DroneDto("1234D", ModelDrone.Lightweight, 100, 100, StateDrone.IDLE);
		assertEquals(service.registeringDrone(newDrone), newDrone);
		Drone drone = droneRepo.findById(newDrone.serialNumber()).orElse(null);
		assertEquals(drone.build(), newDrone);
		assertThrowsExactly(IllegalStateException.class,() -> service.registeringDrone(newDrone) );
		
	}
}

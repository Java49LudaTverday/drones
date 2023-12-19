package telran.drones.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.drones.dto.DroneDto;
import telran.drones.dto.ModelDrone;
import telran.drones.dto.StateDrone;

@Entity
@Table(name="drones")
@NoArgsConstructor
public class Drone {
	@Id
	String serialNumber;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	ModelDrone model;
	@Column(nullable = false)
	int weight;
	@Column(nullable = false)
	int batteryLevel;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	StateDrone state;
	
	public Drone (DroneDto droneDto) {
		serialNumber = droneDto.serialNumber();
		model = droneDto.model();
		weight = droneDto.weight();
		batteryLevel = droneDto.butteryLevel();
		state = droneDto.state();
	}

}

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
	int butteryLevel;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	StateDrone state;
	
	public Drone (DroneDto droneDto) {
		serialNumber = droneDto.serialNumber();
		model = droneDto.model();
		weight = droneDto.weight();
		butteryLevel = droneDto.butteryLevel();
		state = droneDto.state();
	}

	public DroneDto build () {
		return new DroneDto(serialNumber, model, butteryLevel, butteryLevel, state);
	}

	public StateDrone getState() {
		return state;
	}

	public void setState(StateDrone state) {
		this.state = state;
	}

	public int getButteryLevel() {
		return butteryLevel;
	}

	public void setButteryLevel(int butteryLevel) {
		this.butteryLevel = butteryLevel;
	}

}

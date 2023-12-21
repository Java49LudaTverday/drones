package telran.drones.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.DroneDto;
import telran.drones.dto.ModelType;
import telran.drones.dto.State;

@Entity
@Table(name="drones")
@NoArgsConstructor
@Getter
public class Drone {
	@Id
	String serialNumber;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	ModelType model;
	@Column(name="weight_limit", nullable = false)
	int weightLimit;
	@Column(nullable = false)
	byte butteryLevel;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	State state;
	
	public Drone (DroneDto droneDto) {
		serialNumber = droneDto.serialNumber();
		model = droneDto.model();
		weightLimit = droneDto.weight();
		butteryLevel = droneDto.butteryLevel();
		state = droneDto.state();
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setButteryLevel(byte butteryLevel) {
		this.butteryLevel = butteryLevel;
	}

}

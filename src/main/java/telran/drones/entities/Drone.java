package telran.drones.entities;

import jakarta.persistence.*;
import lombok.*;
import telran.drones.dto.*;

@Entity
@Table(name="drones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Drone {
	@Id()
	@Column(name="serial_number")
	String serialNumber;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	ModelType model;
	@Column(name="weight_limit", nullable = false, updatable = false)
	int weightLimit;
	@Column(name="battery_level", nullable = false, updatable = true)
	byte batteryLevel;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = true)
	State state;	

	public Drone (DroneDto droneDto) {
		this.serialNumber = droneDto.getSerialNumber();
		this.model = droneDto.getModel();
		this.state = droneDto.getState();
		this.batteryLevel = droneDto.getBatteryLevel();
		this.weightLimit = droneDto.getWeight();
	}
	public DroneDto buildDto () {
		return new DroneDto(serialNumber, model, weightLimit, batteryLevel, state);
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public void setBatteryLevel(byte batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

}

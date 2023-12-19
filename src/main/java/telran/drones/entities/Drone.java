package telran.drones.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.drones.dto.ModelDrone;
import telran.drones.dto.StateDrone;

@Entity
@Table(name="drones")
@NoArgsConstructor
public class Drone {
	@Id
	int id;
	@Column(nullable = false)
	ModelDrone model;
	@Column(nullable = false)
	int weight;
	@Column(nullable = false)
	int batteryCapacity;
	@Column(nullable = false)
	StateDrone state;

}

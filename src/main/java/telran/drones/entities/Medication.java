package telran.drones.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medications")
@NoArgsConstructor
public class Medication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	int weight;
	@Column(nullable = false)
	String code;
	
	public Medication(String name, int weight, String code) {
		super();
		this.name = name;
		this.weight = weight;
		this.code = code;
	}	

}

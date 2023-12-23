package telran.drones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.MedicationDto;

@Entity
@Table(name = "medications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Medication {

	@Id
	String code;
	@Column(nullable = false)
	String name;
	@Column(nullable = false)
	int weight;
	
	
	public MedicationDto buildDto () {
		return new MedicationDto(code, name, weight);
	}

}

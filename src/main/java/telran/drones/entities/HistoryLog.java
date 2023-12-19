package telran.drones.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import telran.drones.dto.HistoryLogDto;

@Entity
@Table(name = "history_log")
@NoArgsConstructor
public class HistoryLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
//	 @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	@CreationTimestamp
	LocalDateTime timestamp;
	
	@Column(name = "buttery_level", nullable = false)
	int butteryPersent;
	
	@ManyToOne
	@Column(name = "drone", nullable = false)
	Drone drone;
	
	@ManyToOne
	@Column(name = "medication")
	Medication medication;

	public HistoryLog( Drone drone, Medication medication) {
		super();
		this.butteryPersent = drone.batteryLevel;
		this.drone = drone;
		this.medication = medication;
	}
	
	public HistoryLogDto build () {
		return new HistoryLogDto(timestamp,butteryPersent,drone,medication);
	}
	
	

}

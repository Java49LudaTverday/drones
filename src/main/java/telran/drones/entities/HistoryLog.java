package telran.drones.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.drones.dto.HistoryLogDto;
import telran.drones.dto.State;

@Entity
@Table(name = "history_log")
@NoArgsConstructor
@Getter
public class HistoryLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
//	 @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	@CreationTimestamp
	LocalDateTime timestamp;
	
	@Column(name = "buttery_level", nullable = false)
	byte batteryPersent;
	
	@Column(name = "state")
	State state;
	@ManyToOne
	@JoinColumn(name="drone", nullable = false)
	Drone drone;
	
	@ManyToOne
	@JoinColumn(name="medication", nullable = true)
	Medication medication;

	public HistoryLog( Drone drone, Medication medication) {
		super();
		this.state = drone.state;
		this.batteryPersent = drone.batteryLevel;
		this.drone = drone;
		this.medication = medication;
	}
	
	public HistoryLogDto buildDto () {
		return new HistoryLogDto(timestamp, state, batteryPersent, drone, medication);
	}
	
}

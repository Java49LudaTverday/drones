package telran.drones.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.entities.HistoryLog;
import telran.drones.entities.Medication;

public interface HistoryLogRepo extends JpaRepository<HistoryLog, Integer> {
//  @Query("SELECT hl.medication from HistoryLog hl group by drone.id having drone.id=:droneId AND timestamp =:timestamp")
//	List<Medication> findByDroneIdAndTimestamp(String droneId, LocalDateTime timestamp);
}

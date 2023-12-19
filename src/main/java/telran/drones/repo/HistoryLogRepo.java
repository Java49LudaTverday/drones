package telran.drones.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.drones.entities.HistoryLog;

public interface HistoryLogRepo extends JpaRepository<HistoryLog, Integer> {

}

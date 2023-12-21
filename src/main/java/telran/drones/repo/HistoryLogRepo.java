package telran.drones.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import telran.drones.entities.*;


public interface HistoryLogRepo extends JpaRepository<HistoryLog, Integer> {
  
	List<Medication> findByDroneId(String serialumber);
}

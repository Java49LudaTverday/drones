package telran.drones.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.drones.dto.DroneItems;
import telran.drones.dto.State;
import telran.drones.entities.*;


public interface HistoryLogRepo extends JpaRepository<HistoryLog, Long> {
  @Query("Select hl.medication from HistoryLog hl group by drone.serialNumber")
	List<Medication> findByDrone(String serialNumber);
  
//  @Query("Select hl from HistoryLog hl group by drone.serialNumber having drone.serialNumber=:serialNumber")
  List<HistoryLog> findByDroneSerialNumber(String serialNumber);
  
  @Query("Select d.serialNumber as number, count(medication.code) as items from HistoryLog hl "
  		+ "right join Drone d on d.serialNumber=hl.drone.serialNumber group by d.serialNumber")
  List<DroneItems> findNumberOfMedicationByDrones();
  
//  @Query("Select LAST(hl) from HistoryLog hl where drone.serialNumber=:numberDrone AND "
//  		+ "drone.state=:state oreder by hl.timtimestamp")
//  HistoryLog findLastLogForDrone(String numberDrone, State state);
} 

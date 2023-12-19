package telran.drones.service;

import java.util.List;
import telran.drones.dto.*;

public interface DroneService {
DroneDto registeringDrone(DroneDto droneDto);
HistoryLogDto loadingDroneWithMedication (String serialNumber, String medicationCode);
boolean cheackLoadedMedicationsForDrone (String serialNumber, List<MedicationDto> medicationItems);
List<DroneDto> availableDrones (int weight);
int buttaryLevel (int idDrone);
List<String> getHistoryOfDrone (int idDrone);
List<DroneItem> findMedicationsForAllDrones ();
}

package telran.drones.service;

import java.util.List;
import telran.drones.dto.*;

public interface DroneService {
DroneDto registerDrone(DroneDto droneDto);
HistoryLogDto loadDroneWithMedication (String droneNumber, String medicationCode);
List<MedicationDto> checkMedicationItems (String droneNumber);
List<DroneDto> checkAvailableDrones ();
byte buttaryLevel (String droneNumber);
List<HistoryLogDto> checkHistoryLog (String droneNumber);
List<DroneItems> checkLoadedMedicationsByDrones();
}

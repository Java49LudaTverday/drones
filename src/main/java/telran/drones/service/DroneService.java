package telran.drones.service;

import java.util.List;
import telran.drones.dto.*;

public interface DroneService {
DroneDto registerDrone(DroneDto droneDto);
HistoryLogDto loadDroneWithMedication (String droneNumber, String medicationCode);
List<MedicationDto> getMedicationItems (String droneNumber);
List<DroneDto> checkAvailableDrones ();
int checkBatteryLevel (String droneNumber);
List<HistoryLogDto> getHistoryLog (String droneNumber);
List<DroneItems> getLoadedMedicationsByDrones();
}

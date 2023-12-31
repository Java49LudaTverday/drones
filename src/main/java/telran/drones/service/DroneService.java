package telran.drones.service;

import java.util.List;
import telran.drones.dto.*;
import telran.drones.entities.Drone;
import telran.drones.entities.Medication;

public interface DroneService {
DroneDto registerDrone(DroneDto droneDto);
HistoryLogDto loadDroneWithMedication (String droneNumber, String medicationCode);
List<MedicationDto> getMedicationItems (String droneNumber);
List<DroneDto> getAvailableDrones ();
int checkBatteryLevel (String droneNumber);
List<HistoryLogDto> getHistoryLog (String droneNumber);
List<DroneItems> getLoadedMedicationsByDrones();
void periodicTask(Drone drone, Medication medication);
}

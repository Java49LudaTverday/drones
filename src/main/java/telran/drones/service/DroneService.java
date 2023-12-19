package telran.drones.service;

import java.util.List;

import telran.drones.dto.DroneDto;
import telran.drones.dto.DroneItem;
import telran.drones.dto.MedicationDto;

public interface DroneService {
DroneDto registeringDrone(DroneDto droneDto);
boolean loadingDroneWithMedication (MedicationDto medicationDto);
boolean cheakMedicationForDrone (DroneDto droneDto, MedicationDto medicationDto);
List<DroneDto> availableDrones (int weight);
int buttaryLevel (int idDrone);
List<String> getHistoryOfDrone (int idDrone);
List<DroneItem> findMedicationsForAllDrones ();
}

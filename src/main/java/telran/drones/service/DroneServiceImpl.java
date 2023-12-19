package telran.drones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.drones.dto.*;
import telran.drones.entities.*;
import telran.drones.exeption.DroneNotAvailable;
import telran.drones.exeption.DroneOverWeight;
import telran.drones.exeption.LowLevelButtery;
import telran.drones.exeption.NotFoundException;
import telran.drones.repo.*;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
	private static final int MIN_BUTTERY_LEVEL = 25;
	final DroneRepo droneRepo;
	final MedicationRepo medicationRepo;
	final HistoryLogRepo historyLogRepo;

	@Override
	public DroneDto registeringDrone(DroneDto droneDto) {
		if (droneRepo.existsById(droneDto.serialNumber())) {
			throw new IllegalStateException(
					String.format("Drone with serial number %s alredy exists", droneDto.serialNumber()));
		}
		droneRepo.save(new Drone(droneDto));
		return droneDto;
	}

	@Override
	public HistoryLogDto loadingDroneWithMedication(String serialNumber, String medicationCode) {
		Drone drone = droneRepo.findById(serialNumber).orElseThrow(
				() -> new NotFoundException(String.format("drone with serial number %s not found", serialNumber)));
		Medication medication = medicationRepo.findById(medicationCode).orElseThrow(
				() -> new NotFoundException(String.format("medication item with code %s not exists", medicationCode)));
		drone.setState(StateDrone.LOADING);
		HistoryLog loadingDrone = new HistoryLog(drone, medication);
		historyLogRepo.save(loadingDrone);
		return loadingDrone.build();
	}

	@Override
	public boolean cheackLoadedMedicationsForDrone(String serialNumber, List<MedicationDto> medicationItems) {
		Drone drone = droneRepo.findById(serialNumber).orElseThrow(
				() -> new NotFoundException(String.format("drone with serial number %s not exist ", serialNumber)));
		if (drone.getButteryLevel() < MIN_BUTTERY_LEVEL) {
			throw new LowLevelButtery(
					String.format("level buttery %d for drone %s", drone.getButteryLevel(), drone.getSerialNumber()));
		}
		int weight = getWeightOfMedications(medicationItems);
		if (drone.getWeight() < weight) {
			throw new DroneOverWeight(String.format("medication with code %s is overweight for drone %s", weight,
					drone.getSerialNumber()));
		}
		drone.setState(StateDrone.LOADED);
		HistoryLog loadedDrone = new HistoryLog(drone, null);
		historyLogRepo.save(loadedDrone);
		return true;
	}

	private int getWeightOfMedications(List<MedicationDto> medicationItems) {

		return medicationItems.stream().reduce(0, (subtotal, mi) -> subtotal + mi.weight(), Integer::sum);
	}

	@Override
	public List<DroneDto> availableDrones(int weight) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int buttaryLevel(int idDrone) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getHistoryOfDrone(int idDrone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DroneItem> findMedicationsForAllDrones() {
		// TODO Auto-generated method stub
		return null;
	}

}

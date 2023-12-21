package telran.drones.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.drones.api.PropertiesNames;
import telran.drones.configuration.DronesConfiguration;
import telran.drones.dto.*;
import telran.drones.entities.*;
import telran.drones.exceptions.*;
import telran.drones.repo.*;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
	final DroneRepo droneRepo;
	final MedicationRepo medicationRepo;
	final HistoryLogRepo historyLogRepo;
	final ModelMapper modelMapper;
	@Value("${" + PropertiesNames.LEVEL_THRESHOLD + ":25")
	byte levelThreshold;
	
	@Override
	public DroneDto registerDrone(DroneDto droneDto) {
		if (droneRepo.existsById(droneDto.serialNumber())) {
			throw new DroneAlreadyExistException();
		}
		Drone drone = modelMapper.map(droneDto, Drone.class);
		drone.setState(State.IDLE);
		droneRepo.save(drone);
		return droneDto;
	}

	@Override
	public HistoryLogDto loadDroneWithMedication(String droneNumber, String medicationCode) {
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(
				() -> new DroneNotFoundException());
		Medication medication = medicationRepo.findById(medicationCode).orElseThrow(
				() -> new MedicationNotFoundException());
		if(drone.getState() != State.IDLE) {
			throw new IllegalDroneStateException();
		}
		if(drone.getButteryLevel() < levelThreshold) {
			throw new LowBatteryLevelException();
		}
		if(drone.getWeightLimit() < medication.getWeight()) {
			throw new IllegalMedicationWeightException();
		}
		drone.setState(State.LOADING);
		HistoryLog loadDroneLog = new HistoryLog(drone, medication);
		historyLogRepo.save(loadDroneLog);
		HistoryLogDto historyLogDto = modelMapper.map(loadDroneLog,HistoryLogDto.class);
		return historyLogDto;
	}

	@Override
	public List<MedicationDto> checkMedicationItems(String serialNumber) {
		if(!droneRepo.existsById(serialNumber)) {
			new DroneNotFoundException();
		}
		return historyLogRepo.findByDroneId(serialNumber).stream()
				.map(m -> modelMapper.map(m, MedicationDto.class)).toList();
	}

	@Override
	public List<DroneDto> checkAvailableDrones() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte buttaryLevel(String droneNumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<HistoryLogDto> checkHistoryLog(int droneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DroneItems> checkLoadedMedicationsByDrones() {
		// TODO Auto-generated method stub
		return null;
	}

}

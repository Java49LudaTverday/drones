package telran.drones.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.drones.dto.*;
import telran.drones.entities.*;
import telran.drones.exceptions.*;
import telran.drones.repo.*;
import telran.drones.api.*;

@Service
@RequiredArgsConstructor 
@Transactional(readOnly=true)
public class DroneServiceImpl implements DroneService {
final DroneRepo droneRepo;
final MedicationRepo medicationRepo;
final HistoryLogRepo historyLogRepo;
final ModelMapper modelMapper;
@Value("${" + PropertiesNames.CAPACITY_THRESHOLD + ":25}")
byte capacityThreshold;
	@Override
	@Transactional(readOnly=false)
	public DroneDto registerDrone(DroneDto droneDto) {
		if(droneRepo.existsById(droneDto.getSerialNumber())) {
			throw new DroneAlreadyExistException();
		}
		Drone drone = new Drone(droneDto);
		drone.setState(State.IDLE);
		HistoryLog registeringLog = new HistoryLog(drone, null);		
		droneRepo.save(drone);
		historyLogRepo.save(registeringLog);
		return droneDto;
	}

	@Override
	@Transactional(readOnly = false)
	public HistoryLogDto loadDroneWithMedication(String droneNumber, String medicationCode) {
		Drone drone = droneRepo.findById(droneNumber).
				orElseThrow(() -> new DroneNotFoundException());
		Medication medication = medicationRepo.findById(medicationCode).
				orElseThrow(() -> new MedicationNotFoundException());
		if(drone.getState() != State.IDLE) {
			throw new IllegalStateDroneException();
		}
		if(drone.getBatteryLevel() < capacityThreshold) {
			throw new LowBatteryLevelException();
		}
		if(drone.getWeightLimit() < medication.getWeight()) {
			throw new IllegalMedicationWeightException();
		}
		drone.setState(State.LOADING);
		HistoryLog log = new HistoryLog (drone, medication);
		historyLogRepo.save(log);
		HistoryLogDto logDto = log.buildDto();
		return logDto;
	}

	@Override
	public List<MedicationDto> getMedicationItems(String droneNumber) {
		if(!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDrone(droneNumber).stream()
				.map(med -> modelMapper.map(med, MedicationDto.class)).toList();
	}

	@Override
	public List<DroneDto> getAvailableDrones() {
		
		return droneRepo.findByStateAndBatteryLevelGreaterThan(State.IDLE, capacityThreshold).stream().map(d -> d.buildDto()).toList();
	}

	@Override
	public int checkBatteryLevel(String droneNumber) {
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(
				() -> new DroneNotFoundException() );
		
		return drone.getBatteryLevel();
	}

	@Override
	public List<HistoryLogDto> getHistoryLog(String droneNumber) {
		if(!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDroneSerialNumber(droneNumber).
				stream().map(hl -> hl.buildDto()).toList();
	}

	@Override
	public List<DroneItems> getLoadedMedicationsByDrones() {
		
		return historyLogRepo.findNumberOfMedicationByDrones();
	}

}

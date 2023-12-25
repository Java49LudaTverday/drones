package telran.drones.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.*;
import telran.drones.entities.*;
import telran.drones.exceptions.*;
import telran.drones.repo.*;
import telran.drones.api.*;
import telran.drones.controller.DroneController;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DroneServiceImpl implements DroneService {
	final DroneRepo droneRepo;
	final MedicationRepo medicationRepo;
	final HistoryLogRepo historyLogRepo;
	final ModelMapper modelMapper;
	final Map<State, State> mapStates;
	@Value("${" + PropertiesNames.CAPACITY_THRESHOLD + ":25}")
	byte capacityThreshold;
	@Value("${" + PropertiesNames.PEREODIC_UNIT_MICROS + ":100}")
	long millisPerTimeUnit;

	@Override
	@Transactional(readOnly = false)
	public DroneDto registerDrone(DroneDto droneDto) {
		if (droneRepo.existsById(droneDto.getSerialNumber())) {
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
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(() -> new DroneNotFoundException());
		Medication medication = medicationRepo.findById(medicationCode)
				.orElseThrow(() -> new MedicationNotFoundException());
		if (drone.getState() != State.IDLE) {
			throw new IllegalStateDroneException();
		}
		if (drone.getBatteryLevel() < capacityThreshold) {
			throw new LowBatteryLevelException();
		}
		if (drone.getWeightLimit() < medication.getWeight()) {
			throw new IllegalMedicationWeightException();
		}
		drone.setState(State.LOADING);
		HistoryLog log = new HistoryLog(drone, medication);
		historyLogRepo.save(log);
		HistoryLogDto logDto = log.buildDto();
		periodicTask(drone, medication);
		return logDto;
	}

	@Override
	public List<MedicationDto> getMedicationItems(String droneNumber) {
		if (!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDrone(droneNumber).stream().map(med -> modelMapper.map(med, MedicationDto.class))
				.toList();
	}

	@Override
	public List<DroneDto> getAvailableDrones() {

		return droneRepo.findByStateAndBatteryLevelGreaterThan(State.IDLE, capacityThreshold).stream()
				.map(d -> d.buildDto()).toList();
	}

	@Override
	public int checkBatteryLevel(String droneNumber) {
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(() -> new DroneNotFoundException());

		return drone.getBatteryLevel();
	}

	@Override
	public List<HistoryLogDto> getHistoryLog(String droneNumber) {
		if (!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDroneSerialNumber(droneNumber).stream().map(hl -> hl.buildDto()).toList();
	}

	@Override
	public List<DroneItems> getLoadedMedicationsByDrones() {

		return historyLogRepo.findNumberOfMedicationByDrones();
	}

//	@PostConstruct
	public void periodicTask(Drone drone, Medication medication) {		 
		Thread thread = new Thread(() -> {	
			boolean flag = true;
				while (flag) {
					try {
						Thread.sleep(millisPerTimeUnit);
						State state = drone.getState();
						switch(state) {
						case LOADING ->changeState(drone, medication);
						case LOADED -> changeState(drone, medication);
						case DELIVERING -> changeState(drone, medication);
						case DELIVERING1 -> changeState(drone, medication);
						case DELIVERING2 -> changeState(drone, medication);
						case DELIVERING3 -> changeState( drone, medication);
						case DELIVERED -> changeState( drone, null);
						case RETURNING -> changeState( drone, null);
						case RETURNING1 -> changeState( drone, null);
						case RETURNING2 -> changeState( drone, null);
						case RETURNING3 -> changeState( drone, null);
						case IDLE -> flag =  false;
						default -> throw new IllegalArgumentException("Unexpected state: " + state);
						}
					} catch (InterruptedException e) {
						// Interruption are not implemented
					}
				}
			});
			// TODO Processing event logs for each time unit
			// Just stub method to be replaced with a real one in the HW#71

		thread.setDaemon(true);
		thread.start();
	}

	private boolean changeState(Drone drone, Medication medication) {
		State newState = mapStates.get(drone.getState());
		System.out.println("NewState" +newState);
		drone.setState(newState);
		setNewBatteryLevel(drone);
		droneRepo.save(drone);
		setHistoryLog(drone, medication);
		return true;
	}

	private void setHistoryLog(Drone drone, Medication medication) {
		HistoryLog historyLog = new HistoryLog(drone, medication);
		historyLogRepo.save(historyLog);
		
	}

	private void setNewBatteryLevel(Drone drone) {
		byte newLevel = (byte) (drone.getBatteryLevel() - ConstraintConstant.PERCENT_DECREASE_BATTERY);
		drone.setBatteryLevel(newLevel);
		droneRepo.save(drone);
		
	}

}

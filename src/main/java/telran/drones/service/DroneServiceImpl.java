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
//@Transactional(readOnly = true)
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
		return logDto;
	}

	@Override
	@Transactional(readOnly = false)
	public List<MedicationDto> getMedicationItems(String droneNumber) {
		if (!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDrone(droneNumber).stream().map(med -> modelMapper.map(med, MedicationDto.class))
				.toList();
	}

	@Override
	@Transactional(readOnly = false)
	public List<DroneDto> getAvailableDrones() {

		return droneRepo.findByStateAndBatteryLevelGreaterThan(State.IDLE, capacityThreshold).stream()
				.map(d -> d.buildDto()).toList();
	}

	@Override
	@Transactional(readOnly = false)
	public int checkBatteryLevel(String droneNumber) {
		Drone drone = droneRepo.findById(droneNumber).orElseThrow(() -> new DroneNotFoundException());

		return drone.getBatteryLevel();
	}

	@Override
	@Transactional(readOnly = false)
	public List<HistoryLogDto> getHistoryLog(String droneNumber) {
		if (!droneRepo.existsById(droneNumber)) {
			throw new DroneNotFoundException();
		}
		return historyLogRepo.findByDroneSerialNumber(droneNumber).stream().map(hl -> hl.buildDto()).toList();
	}

	@Override
	@Transactional(readOnly = false)
	public List<DroneItems> getLoadedMedicationsByDrones() {

		return historyLogRepo.findNumberOfMedicationByDrones();
	}

	@PostConstruct
	void periodicTask() throws InterruptedException {
		createThreadPeriodicTask();
	}

	private void createThreadPeriodicTask() {
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// not implemented
			}
			while (true) {
				List<Drone> drones = droneRepo.findAll();
				drones.forEach(drone -> {
					try {						
						changeStateOfDrone(drone);						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	private void changeStateOfDrone(Drone drone) throws InterruptedException {
		State state = drone.getState();
		Thread.sleep(millisPerTimeUnit);
		if (state != State.IDLE) {
			State newState = mapStates.get(state);
			drone.setState(newState);
			decreaseBatteryLevel(drone);
		} else {
			increaseBatteryLevel(drone);
		}
	}

	private void increaseBatteryLevel(Drone drone) {
		byte butteryLevel = drone.getBatteryLevel();
		if (butteryLevel < 99) {
			butteryLevel += ConstraintConstant.PERCENT_DECREASE_BATTERY;
			drone.setBatteryLevel(butteryLevel);
			droneRepo.save(drone);
			setHistoryLog(drone, null);
		}
	}

	private void setHistoryLog(Drone drone, Medication medication) {
		HistoryLog historyLog = new HistoryLog(drone, medication);
		historyLogRepo.save(historyLog);
	}

	private void decreaseBatteryLevel(Drone drone) {
		byte newLevel = (byte) (drone.getBatteryLevel() - ConstraintConstant.PERCENT_DECREASE_BATTERY);
		drone.setBatteryLevel(newLevel);
		droneRepo.save(drone);
		setHistoryLog(drone, null);

	}

}

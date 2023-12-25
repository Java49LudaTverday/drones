package telran.drones.test;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.drones.api.ServiceExceptionMessages;
import telran.drones.dto.*;
import telran.drones.entities.Drone;
import telran.drones.entities.HistoryLog;
import telran.drones.exceptions.DroneAlreadyExistException;
import telran.drones.exceptions.IllegalMedicationWeightException;
import telran.drones.exceptions.IllegalStateDroneException;
import telran.drones.exceptions.LowBatteryLevelException;
import telran.drones.repo.DroneRepo;
import telran.drones.repo.HistoryLogRepo;
import telran.drones.repo.MedicationRepo;
import telran.drones.service.DroneService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@Sql(scripts = "test_data.sql")
public class DronesServiceStaticTest {
	@Autowired
	DroneService service;
	@Autowired
	DroneRepo droneRepo;
	@Autowired
	HistoryLogRepo historyLogRepo;
	@Autowired
	MedicationRepo medicationRepo;
	@Autowired
	ModelMapper modelMapper;

	private static final String DRONE_1 = "Drone-1";
	private static final String DRONE_2 = "Drone-2";
	private static final String MED_1 = "MED_1";
	private static final String DRONE_3 = "Drone-3";
	private static final String MED_2 = "MED_2";
	private static final String SERVICE_TEST = "Service ";
	private static final byte LOW_BATTERY_LEVEL = 24;
	private static final int BATTERY_LEVEL_100 = 100;
	private static final int BATTERY_LEVEL_20 = 20;

	DroneDto droneDto = new DroneDto("D-123", ModelType.Middleweight, 300, (byte) 100, State.IDLE);
	DroneDto droneDtoAlreadyExist = new DroneDto("Drone-1", ModelType.Middleweight, 300, (byte) 100, State.IDLE);
	MedicationDto medicationDto = new MedicationDto("CODE_1", "Medication-1", 200);

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.REGISTER_NORMAL_DRONE)
	void registerNormalDroneTest() {
		DroneDto res = service.registerDrone(droneDto);
		assertEquals(res, droneDto);
		Drone drone = droneRepo.findById(droneDto.getSerialNumber()).orElse(null);
		assertEquals(drone.getSerialNumber(), droneDto.getSerialNumber());
		List<HistoryLog> logs = historyLogRepo.findAll();
		String serialNumber = logs.get(0).getDrone().getSerialNumber();
		assertEquals(serialNumber, drone.getSerialNumber());

	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.REGISTER_DRONE_ALREDY_EXIST)
	void registerAlreadyExistsDroneTest() {
		assertThrowsExactly(DroneAlreadyExistException.class, 
				() -> service.registerDrone(droneDtoAlreadyExist));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_NORMAL)
	void loadNormalDrone() {
		service.loadDroneWithMedication(DRONE_1, MED_1);
		List<HistoryLog> logs = historyLogRepo.findAll();
		assertEquals(1, logs.size());
		HistoryLog loadingLog = logs.get(0);
		Drone drone = loadingLog.getDrone();
		assertEquals(DRONE_1, drone.getSerialNumber());
		assertEquals(State.LOADING, drone.getState());
		assertEquals(MED_1, loadingLog.getMedication().getCode());
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_NOT_MATCHING_STATE)
	void loadDroneWrongState() {
		assertThrowsExactly(IllegalStateDroneException.class, () -> service.loadDroneWithMedication(DRONE_3, MED_1));

	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_DRONE_LOW_BATTERY)
	void loadDroneLowBatteryLevelTest() {
		Drone drone = droneRepo.findById(DRONE_1).orElse(null);
		drone.setBatteryLevel(LOW_BATTERY_LEVEL);
		droneRepo.save(drone);
		assertThrowsExactly(LowBatteryLevelException.class, () -> service.loadDroneWithMedication(DRONE_1, MED_1),
				ServiceExceptionMessages.LOW_BATTERY_LEVEL);
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.LOAD_OVER_WEIGHT)
	void loadDroneIllegalWeightTest() {
		assertThrowsExactly(IllegalMedicationWeightException.class,
				() -> service.loadDroneWithMedication(DRONE_1, MED_2), ServiceExceptionMessages.WEIGHT_LIMIT_VIOLATION);
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.GET_MEDICATION_ITEMS)
	void checkMedicationItemsTest() {
		service.loadDroneWithMedication(DRONE_1, MED_1);
		List<MedicationDto> actual = service.getMedicationItems(DRONE_1);
		MedicationDto expected = medicationRepo.findById(MED_1).orElse(null).buildDto();
		assertEquals(expected, actual.get(0));
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.CHECK_AVAILABLE_DRONES)
	void checkAvailableDronesTest() {
		List<DroneDto> availableDrones = service.getAvailableDrones();
		Drone expected = droneRepo.findById(DRONE_1).orElse(null);
		List<Drone> actual = availableDrones.stream().map(ad -> new Drone(ad)).toList();
		assertEquals(expected, actual.get(0));

	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.CHECK_BATTERY_LEVEL)
	void checkBatteryLevelTest() {
		int batteryLevelDrone1 = service.checkBatteryLevel(DRONE_1);
		assertEquals(BATTERY_LEVEL_100, batteryLevelDrone1);
		int batteryLevelDrone2 = service.checkBatteryLevel(DRONE_2);
		assertEquals(BATTERY_LEVEL_20, batteryLevelDrone2);
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.GET_HISTORY_LOG)
	void checkHistoryLogTest() {
		HistoryLogDto loadHistoryLog = service.loadDroneWithMedication(DRONE_1, MED_1);
		List<HistoryLogDto> actualHistoryLog = service.getHistoryLog(DRONE_1);
		assertEquals(1, actualHistoryLog.size());
		assertEquals(loadHistoryLog, actualHistoryLog.get(0));		
	}

	@Test
	@DisplayName(SERVICE_TEST + TestDisplayNames.GET_LOADED_MEDICATION)
	void checkLoadedMedicationByDronesTest() {
		service.loadDroneWithMedication(DRONE_1, MED_1);
		Drone drone = droneRepo.findById(DRONE_3).orElse(null);
		drone.setState(State.IDLE);
		droneRepo.save(drone);
		service.loadDroneWithMedication(DRONE_3, MED_1);
		Map<String, Integer> expected = Map.of(DRONE_1, 1, DRONE_2, 0, DRONE_3, 1);
		
		List<DroneItems> actual = service.getLoadedMedicationsByDrones();
		actual.forEach((v)-> System.out.println(v.getItems()));
		assertEquals(expected.size(), actual.size());
		actual.forEach(di -> assertEquals(di.getItems(), expected.get(di.getNumber())));		
	}

}

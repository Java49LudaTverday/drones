package telran.drones.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.drones.api.PropertiesNames;
import telran.drones.dto.DroneDto;
import telran.drones.dto.HistoryLogDto;
import telran.drones.dto.ModelType;
import telran.drones.dto.State;
import telran.drones.entities.Medication;
import telran.drones.repo.MedicationRepo;
import telran.drones.service.DroneService;

@SpringBootTest(properties = {PropertiesNames.PEREODIC_UNIT_MICROS + "=100"})
//('Drone-1', 'Middleweight', 300, 100, 'IDLE'),
//('Drone-2', 'Middleweight', 300, 20, 'IDLE'),
//('Drone-3', 'Middleweight', 300, 100, 'LOADING');

//('MED_1', 'Medication-1', 200),
//('MED_2', 'Medication-2', 350); 
class DronesServicePeriodicTaskTest {
	private static final String DRONE_1 = "Drone-1";
	private static final String MED_1 = "MED_1";
	@Autowired
	DroneService service;

	@Test
	void test() throws InterruptedException {
		List<HistoryLogDto> logsBefore = service.getHistoryLog(DRONE_1);
		assertTrue(logsBefore.isEmpty());
		service.loadDroneWithMedication(DRONE_1, MED_1);
		Thread.sleep(3800);
		List<HistoryLogDto> logsAfter = service.getHistoryLog(DRONE_1);
		logsAfter.forEach(ld -> {System.out.println(ld);
		});
		assertEquals(12, logsAfter.size());
		
	}

}

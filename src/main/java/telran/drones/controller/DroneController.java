package telran.drones.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.*;
import telran.drones.service.DroneService;


@RestController
@RequestMapping("drones")
@RequiredArgsConstructor
@Slf4j
public class DroneController {
	final DroneService droneService;
	
	@PostMapping("/add")
	DroneDto registeringDrone (@RequestBody @Valid DroneDto droneDto) {
		log.debug("method : registeringDrone, received {}", droneDto);
		return droneService.registerDrone(droneDto);
	}
	
//	@PostMapping()
//	HistoryLogDto loadDroneWithMedication (String droneNumber,  String medicationCode) {
//		log.debug("method : loadDroneWithMedication, received {}", droneNumber, medicationCode);
//		return droneService.loadDroneWithMedication(droneNumber, medicationCode);
//	}
//	
//	@GetMapping()
//	List<MedicationDto> checkMedicationItems(String droneNumber){
//		log.debug("method : checkMedicationItems, received {}", droneNumber);		
//		return droneService.checkMedicationItems(droneNumber);
//	}
//	
	

}

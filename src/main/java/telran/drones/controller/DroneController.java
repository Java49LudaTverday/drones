package telran.drones.controller;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.*;
import telran.drones.service.DroneService;
import telran.drones.api.UrlConstants;
import static telran.drones.api.UrlConstants.*;
import static telran.drones.api.ConstraintConstant.*;

@RestController
@RequestMapping(UrlConstants.MAIN_MAPPER)
@RequiredArgsConstructor
@Validated
@Slf4j
public class DroneController {
	final DroneService droneService;
	
	@PostMapping(ADD_MAPPER)
	DroneDto registeringDrone (@RequestBody @Valid DroneDto droneDto) {
		log.debug("method : registeringDrone, received {}", droneDto);
		return droneService.registerDrone(droneDto);
	}
	
	@PostMapping(LOAD_MAPPER +"/{number}/{code}")
	HistoryLogDto loadDroneWithMedication (
			@PathVariable("number") @Size(max=MAX_DRONE_NUMBER_SIZE) @NotEmpty String droneNumber,  
			@PathVariable("code") String medicationCode) {
		log.debug("method : loadDroneWithMedication, received {}", droneNumber, medicationCode);
		return droneService.loadDroneWithMedication(droneNumber, medicationCode);
	}
	
	@GetMapping()
	List<MedicationDto> checkMedicationItems(String droneNumber){
		log.debug("method : checkMedicationItems, received {}", droneNumber);		
		return droneService.getMedicationItems(droneNumber);
	}
	
	

}

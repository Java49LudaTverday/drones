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
	
	@GetMapping(ITEMS_MAPPER + "/{number}")
	List<MedicationDto> getMedicationItems(@PathVariable("number") 
	@Size(max=MAX_DRONE_NUMBER_SIZE) @NotEmpty String droneNumber){
		log.debug("method : getMedicationItems, received {}", droneNumber);		
		return droneService.getMedicationItems(droneNumber);
	}
	
	@GetMapping(GET_DRONES_MAPPER)
	List<DroneDto> getAvailableDrones(){
		log.debug("method : getAvailableDrones");		
		return droneService.getAvailableDrones();
	}
	
	@GetMapping(BATTERY_MAPPER + "/{number}")
	int checkBatteryLevel(@PathVariable("number") 
	@Size(max=MAX_DRONE_NUMBER_SIZE) @NotEmpty String droneNumber){
		log.debug("method : checkBatteryLevel , received {}", droneNumber);		
		return droneService.checkBatteryLevel(droneNumber);
	}
	
	@GetMapping(HISTORY_MAPPER + "/{number}")
	List<HistoryLogDto> getHistoryLog (@PathVariable("number") 
	@Size(max=MAX_DRONE_NUMBER_SIZE) @NotEmpty String droneNumber){
		log.debug("method : getHistoryLog, received: {}", droneNumber);		
		return droneService.getHistoryLog(droneNumber);
	}
	@GetMapping(DRONE_ITEMS_MAPPER + "/{number}")
	List<DroneItems> getLoadedMedicationsByDrones(){
		log.debug("method : getLoadedMedicationsByDrones");		
		return droneService.getLoadedMedicationsByDrones();
	}

}

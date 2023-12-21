package telran.drones.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.drones.dto.DroneDto;
import telran.drones.service.DroneService;

@RestController
@RequestMapping("drones")
@RequiredArgsConstructor
@Slf4j
public class DroneController {
	final DroneService droneService;
	
	@PostMapping
	DroneDto registeringDrone (@RequestBody @Valid DroneDto droneDto) {
		log.debug("method : registeringDrone, received {}", droneDto);
		return droneService.registerDrone(droneDto);
	}

}

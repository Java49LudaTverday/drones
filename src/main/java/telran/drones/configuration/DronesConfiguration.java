package telran.drones.configuration;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import telran.drones.dto.State;

import static telran.drones.dto.State.*;
@Configuration
public class DronesConfiguration {
@Bean
ModelMapper getModelMapper() {
	return new ModelMapper();
}

@Bean
Map<State, State > getMovesMap () {
	Map<State, State> res = new HashMap<>();
	res.put(LOADING, LOADED);
	res.put(LOADED, DELIVERING);
	res.put(DELIVERING, DELIVERING1);
	res.put(DELIVERING1, DELIVERING2);
	res.put(DELIVERING2, DELIVERING3);
	res.put(DELIVERING3, DELIVERED);
	res.put(DELIVERED,RETURNING);
	res.put(RETURNING, RETURNING1);
	res.put(RETURNING1, RETURNING2);
	res.put(RETURNING2, RETURNING3);
	res.put(RETURNING3, IDLE);
	return res;
}
}

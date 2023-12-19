package telran.drones.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.drones.controller.DroneController;
import telran.drones.dto.*;
import telran.drones.service.DroneService;

@WebMvcTest
public class DroneControllerTest {

	@Autowired
	DroneController droneController;
	@MockBean
	DroneService droneService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	
	private static final DroneDto normalDrone = new DroneDto("1234D", ModelDrone.Lightweight, 100, 100, StateDrone.IDLE);
	private static final DroneDto wrongWeightDrone = new DroneDto("1235D", ModelDrone.Lightweight, 600, 100, StateDrone.IDLE);
	private static final DroneDto nullStatusDrone = new DroneDto("1235D", ModelDrone.Lightweight, 100, 100, null);

	@Test
	void loadingContextTest() {
		assertNotNull(droneController);
		assertNotNull(droneService);
		assertNotNull(mockMvc);
		assertNotNull(objectMapper);
	}

	@Test
	void registeringDroneNormalFlowTest() throws Exception {
		when(droneService.registeringDrone(normalDrone)).thenReturn(normalDrone);
		String droneJson = objectMapper.writeValueAsString(normalDrone);
		String response = mockMvc.perform(post("http://localhost:8090/drones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(droneJson)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertEquals(droneJson, response);
	}
	@Test
	void alreadyExistRegisteringDroneTest () throws Exception  {
		 String exceptionMessage = "already exists";
		when(droneService.registeringDrone(normalDrone)).thenThrow(new IllegalStateException(exceptionMessage));
		String droneJson = objectMapper.writeValueAsString(normalDrone);
		String response = mockMvc.perform(post("http://localhost:8090/drones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(droneJson)).andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals(exceptionMessage, response);
	}
	@Test
	void registeringDronWrongFlowTest () throws Exception {
		String response = mockMvc.perform(post("http://localhost:8090/drones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongWeightDrone))).andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals("must be less than or equal to 500", response);
	}
	@Test
	void registeringDroneNullStatusTest () throws Exception {
		String response = mockMvc.perform(post("http://localhost:8090/drones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nullStatusDrone))).andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals("must not be null", response);
	}
}

package telran.drones.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolationException;

import static telran.drones.api.ServiceExceptionMessages.*;

import static telran.drones.api.ConstraintConstant.*;
import telran.drones.controller.DroneController;
import telran.drones.dto.*;
import telran.drones.exceptions.DroneNotFoundException;
import telran.drones.exceptions.IllegalMedicationWeightException;
import telran.drones.exceptions.LowBatteryLevelException;
import telran.drones.exceptions.MedicationNotFoundException;
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
	private static final String HOST = "http://localhost:8090/";
	private static final String CONTROLLER_TEST = "Controller ";
	private static final String WRONG_DRONE_NUMBER  = new String(new char[10000]);
	DroneDto normalDrone = new DroneDto("Drone-4", ModelType.Middleweight, 100, (byte) 300, State.IDLE);
	DroneDto droneDtoWrongFields = new DroneDto(WRONG_DRONE_NUMBER, ModelType.Middleweight, 600, (byte) 101,
			State.IDLE);
	DroneDto droneDtoMissingFields = new DroneDto(null, null, null, null, null);
	
	private static final String DRONE_1 = "Drone-1";
	private static final String DRONE_2 = "Drone-2";
	private static final String MED_1 = "MED_1";
	private static final String DRONE_3 = "Drone-3";
	private static final String MED_2 = "MED_2";
	private static final String NOT_EXIST_DRONE_4 = "Drone-4";
	private static final String NOT_EXIST_MED_3 = "MED_3";
	private static final MedicationDto MED_DTO = new MedicationDto();
	private static final HistoryLogDto LOG_DTO = new HistoryLogDto();
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOADING_CONTEXT)
	void loadingContextTest() {
		assertNotNull(droneController);
		assertNotNull(droneService);
		assertNotNull(mockMvc);
		assertNotNull(objectMapper);
	}

	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.REGISTER_NORMAL_DRONE)
	void registeringDroneNormalFlowTest() throws Exception {
		when(droneService.registerDrone(normalDrone)).thenReturn(normalDrone);
		String droneJson = objectMapper.writeValueAsString(normalDrone);
		String response = mockMvc
				.perform(post(HOST + "drones/add")
						.contentType(MediaType.APPLICATION_JSON).content(droneJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse()
				.getContentAsString();
		assertEquals(droneJson, response);
	}

	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.REGISTER_DRONE_ALREDY_EXIST)
	void alreadyExistRegisteringDroneTest() throws Exception {
		String exceptionMessage = "already exists";
		when(droneService.registerDrone(normalDrone)).thenThrow(new IllegalStateException(exceptionMessage));
		String droneJson = objectMapper.writeValueAsString(normalDrone);
		String response = mockMvc
				.perform(post(HOST + "drones/add").contentType(MediaType.APPLICATION_JSON).content(droneJson))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(exceptionMessage, response);
	}

	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.REGISTER_DRONE_WRONG_FIELDS)
	void registeringDronWrongFlowTest() throws Exception {
		String response = mockMvc
				.perform(post(HOST + "drones/add").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(droneDtoWrongFields)))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		String[] expected = {MAX_WEIGHT_VIOLATION, MAX_PERCENTAGE_VIOLATION,
				DRONE_NUMBER_WRONG_LENGTH};
		String[] actual = response.split(";");
		checkArraysOfValidMessages(expected, actual);
	}

	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.REGISTER_DRONE_NULL_FIELDS)
	void registeringDroneNullFieldsTest() throws Exception {
		String response = mockMvc
				.perform(post(HOST + "drones/add").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(droneDtoMissingFields)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		String[] expected = {MISSING_STATE,
				MISSING_MODEL,MISSING_DRONE_NUMBER, MISSING_WEIGHT_LIMIT, MISSING_BATTERY_CAPACITY};
		String[] actual = response.split(";");
		checkArraysOfValidMessages(expected, actual);
	}
	
	private void checkArraysOfValidMessages(String[] expected, String[] actual) {
		Arrays.sort(expected);
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOAD_DRONE_NORMAL)
	void loadDroneNormalFlowTest () throws Exception {
		HistoryLogDto hisLog = new HistoryLogDto();
		when(droneService.loadDroneWithMedication(DRONE_1, MED_1)).thenReturn(hisLog);
		String response = mockMvc.
				perform(post(HOST + "drones/load/{number}/{code}", DRONE_1, MED_1))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		System.out.println("response"+response);
		String  expected = objectMapper.writeValueAsString(hisLog);
		assertEquals(expected, response);
		

	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOAD_DRONE_WRONG_NUMBER)
	void loadDroneWrongSerilNumberTest () throws Exception {
		when(droneService.loadDroneWithMedication(NOT_EXIST_DRONE_4, MED_1))
		.thenThrow(new DroneNotFoundException());
		String response = mockMvc
				.perform(post(HOST + "drones/load/{number}/{code}", NOT_EXIST_DRONE_4, MED_1))
				.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(DRONE_NOT_FOUND, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOAD_DRONE_WRONG_MED_CODE)
	void loadDroneWrongMedicationCodeTest () throws Exception {
		when(droneService.loadDroneWithMedication(DRONE_1,NOT_EXIST_MED_3))
		.thenThrow(new MedicationNotFoundException());
		String response = mockMvc
				.perform(post(HOST + "drones/load/{number}/{code}", DRONE_1, NOT_EXIST_MED_3))
				.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(MEDICATION_NOT_FOUND, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOAD_OVER_WEIGHT)
	void loadDroneIllegalWeighgtTest () throws Exception {
		when(droneService.loadDroneWithMedication(DRONE_1, MED_2))
		.thenThrow(new IllegalMedicationWeightException());
		String response = mockMvc
				.perform(post(HOST + "drones/load/{number}/{code}", DRONE_1, MED_2))
				.andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals(WEIGHT_LIMIT_VIOLATION, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.LOAD_DRONE_LOW_BATTERY)
	void loadDroneIllegalBatteryLevelTest () throws Exception {
		when(droneService.loadDroneWithMedication(DRONE_2, MED_1))
		.thenThrow(new LowBatteryLevelException());
		String response = mockMvc
				.perform(post(HOST + "drones/load/{number}/{code}", DRONE_2, MED_1))
				.andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertEquals(LOW_BATTERY_LEVEL, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_LOADED_MEDICATION)
	void getMedicationItemsTest () throws Exception {
	List<MedicationDto> list = List.of(MED_DTO);	
	when(droneService.getMedicationItems(DRONE_1)).thenReturn(list);
		String response = mockMvc
				.perform(get(HOST + "drones/items/{number}", DRONE_1))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String expected = objectMapper.writeValueAsString(list);
		assertEquals(expected, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_LOADED_MEDICATION_WRONG_DRONE)
	void getMedicationItemsWrongDroneNumberTest () throws Exception {
		when(droneService.getMedicationItems(NOT_EXIST_DRONE_4))
		.thenThrow(new DroneNotFoundException());
		String response = mockMvc
				.perform(get(HOST + "drones/items/{number}", NOT_EXIST_DRONE_4))
				.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(DRONE_NOT_FOUND, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.CHECK_BATTERY_LEVEL)
	void checkBatteryLevelTest () throws Exception {
		int batteryLevel = 100;
		when(droneService.checkBatteryLevel(DRONE_1))
		.thenReturn(batteryLevel);
		String response = mockMvc
				.perform(get(HOST + "drones/battery/{number}", DRONE_1))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertEquals("100", response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.CHECK_BATTERY_LEVEL_WRONG_NUMBER)
	void checkBatteryLevelWrongDroneNumberTest () throws Exception {
		when(droneService.checkBatteryLevel(NOT_EXIST_DRONE_4))
		.thenThrow(new DroneNotFoundException());
		String response = mockMvc
				.perform(get(HOST + "drones/battery/{number}", NOT_EXIST_DRONE_4))
				.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(DRONE_NOT_FOUND, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_HISTORY_LOG)
	void getHistoryLogTest () throws Exception {
		List<HistoryLogDto> logs = List.of(LOG_DTO);
		when(droneService.getHistoryLog(DRONE_1))
		.thenReturn(logs);
		String response = mockMvc
				.perform(get(HOST + "drones/logs/{number}", DRONE_1))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String expected = objectMapper.writeValueAsString(logs);
		assertEquals(expected, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_HISTORY_LOG_WRONG_DRONE)
	void getHistoryLogWrongDroneNumberTest () throws Exception {
		when(droneService.getHistoryLog(NOT_EXIST_DRONE_4))
		.thenThrow(new DroneNotFoundException());
		String response = mockMvc
				.perform(get(HOST + "drones/logs/{number}", NOT_EXIST_DRONE_4))
				.andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(DRONE_NOT_FOUND, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_LOADED_MEDICATION)
	void getLoadedMedicationsByDronesTest () throws Exception {
		List<HistoryLogDto> emptyList = List.of();
		when(droneService.getHistoryLog(DRONE_1))
		.thenReturn(emptyList);
		String response = mockMvc
				.perform(get(HOST + "drones/logs/{number}", DRONE_1))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String expected = objectMapper.writeValueAsString(emptyList);
		assertEquals(expected, response);
	}
	
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.GET_HISTORY_LOG)
	void getAvailableDronesTest () throws Exception {
		List<DroneDto> drones = List.of(normalDrone);
		when(droneService.getAvailableDrones())
		.thenReturn(drones);
		String response = mockMvc
				.perform(get(HOST + "drones/get_drones"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String expected = objectMapper.writeValueAsString(drones);
		assertEquals(expected, response);
	}
	@Test
	@DisplayName(CONTROLLER_TEST + TestDisplayNames.CHECK_BATTERY_LEVEL_ILLEGAL_NUMBER)
	void checkBatteryLevelIllegalDroneTest () throws Exception {
		mockMvc.perform(get(HOST + "drones/battery/{number}", WRONG_DRONE_NUMBER))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(res -> assertTrue(res.getResolvedException() instanceof ConstraintViolationException))
				.andExpect(res -> 
				assertEquals("checkBatteryLevel.droneNumber: size must be between 0 and 100", 
						res.getResolvedException().getMessage()));
	}
	
}

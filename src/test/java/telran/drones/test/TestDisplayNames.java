package telran.drones.test;

public interface TestDisplayNames {
String LOAD_DRONE_NORMAL = "Loading drone normal flow";
String REGISTER_NORMAL_DRONE = "Registering new drone";
String REGISTER_DRONE_ALREDY_EXIST= "Registering drone that alredy exists";
String REGISTER_DRONE_WRONG_FIELDS = "Registering drong with wrong fields";
String REGISTER_DRONE_NULL_FIELDS = "Registering drong with null fields";
String LOAD_DRONE_NOT_MATCHING_STATE = "Loading Drone State not IDLE";
String LOAD_DRONE_LOW_BATTERY = "Loading drone has low battery level";
String LOAD_OVER_WEIGHT = "Loading drone with illegal weight of medication";
String GET_MEDICATION_ITEMS = "Receiving list of loaded medications by drone";
String CHECK_AVAILABLE_DRONES = "Receiving list of available drones";
String CHECK_BATTERY_LEVEL = "Checking battery level for given drone";
String GET_HISTORY_LOG = "Receiving logs history by given drone";
String GET_LOADED_MEDICATION = "Receiving number of loaded medications by each drone";
String LOADING_CONTEXT = "Checking if context has been loaded";
String LOAD_DRONE_WRONG_NUMBER = "Loading drone for wrong serial number";
String LOAD_DRONE_WRONG_MED_CODE = "Loading drone for wrong medication code";
String GET_MEDICATION_ITEMS_WRONG_DRONE = "Checking method getMedicationItems for wrong drone`s number";
String GET_HISTORY_LOG_WRONG_DRONE = "Trying get medication for wrond drone`s number";
String CHECK_BATTERY_LEVEL_WRONG_NUMBER ="Trying check battery level for wrong drone`s number";
String GET_LOADED_MEDICATION_WRONG_DRONE = "Trying get medication items for wrong drone`s number";
String CHECK_BATTERY_LEVEL_ILLEGAL_NUMBER = "Trying send illegal drone`s number and get battery level";

}

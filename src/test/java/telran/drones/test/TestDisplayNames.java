package telran.drones.test;

public interface TestDisplayNames {
String LOAD_DRONE_NORMAL = "Loading drone normal flow";
String REGISTER_NORMAL_DRONE = "Registering new drone";
String REGISTER_ALREDY_EXIST_DRONE = "Registering drone that alredy exists";
String REGISTER_DRONE_WRONG_FIELDS = "Testing registering drong with wrong fields";
String LOAD_DRONE_NOT_MATCHING_STATE = "Loading Drone State not IDLE";
String LOAD_DRONE_LOW_BATTERY = "Loading drone has low battery level";
String LOAD_OVER_WEIGHT = "Loading drone with illegal weight of medication";
String CHECK_MEDICATION_ITEMS = "Receiving list of loaded medications by drone";
String CHECK_AVAILABLE_DRONES = "Receiving list of available drones";
String CHECK_BATTERY_LEVEL = "Checking battery level for given drone";
String CHECK_HISTORY_LOG = "Checking logs history by given drone";
String CHECK_LOADED_MEDICATION = "Checking loaded medications of each drone";

}

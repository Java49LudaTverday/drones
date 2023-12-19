package telran.drones.exeption;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@ControllerAdvice
@Slf4j
public class HandlerExceptions {
	@ExceptionHandler({IllegalStateException.class,
		IllegalArgumentException.class, HttpMessageNotReadableException.class})
	ResponseEntity<String> badRequest(Exception e){		
		String message = e.getMessage();
		log.error("Bad request: {}", message);
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({DroneNotAvailable.class,DroneOverWeight.class,
		LowLevelButtery.class })
	ResponseEntity<String> notMatch(DroneNotAvailable e){
		String message = e.getMessage();
		log.error("Not match : {}", message);
		return new ResponseEntity<String>(message, HttpStatus.NOT_FOUND);
	}
		
	@ExceptionHandler({NotFoundException.class})
	ResponseEntity<String> notFound(NotFoundException e){
		String message = e.getMessage();
		log.error("Not found : {}", message);
		return new ResponseEntity<String>(message, HttpStatus.NOT_FOUND);
	} 
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> notValidHandler(MethodArgumentNotValidException e){
		List<ObjectError> errors = e.getAllErrors();
		String body = errors.stream().map(err -> err.getDefaultMessage())
				.collect(Collectors.joining(";"));
		return errorResponse(body, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<String> errorResponse(String body, HttpStatus status) {
		log.error(body);
		return new ResponseEntity<>(body, status);
	}
	
	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<String> runtimeExceptionHandler (RuntimeException e){
		log.error(e.getMessage());
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

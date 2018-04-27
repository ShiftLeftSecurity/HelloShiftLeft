package exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerExceptionHandler {
	public void handleNotFound(CustomerNotFoundException ex) {
		log.error("Resource not found");
	}

	public void handleBadRequest(InvalidCustomerRequestException ex) {
		log.error("Invalid Fund Request");
	}

	public void handleGeneralError(Exception ex) {
		log.error("An error occurred procesing request", ex);
	}
}

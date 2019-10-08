package benefitBountyService.exceptions;

public class BadInputException extends Exception {
    public BadInputException(String input) {
        super(input);
    }
}

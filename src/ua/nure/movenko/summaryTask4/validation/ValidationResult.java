package ua.nure.movenko.summaryTask4.validation;

import ua.nure.movenko.summaryTask4.enums.ValidationStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationResult {

    private ValidationStatus validationStatus;
    private final Map<String, String> errorLog = new HashMap<>();

    public ValidationResult(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    /**
     * Creates new ValidationResult object fulfilling {@code errorLog} map
     *
     * @param validationStatus is VakidationStatus entity object
     * @param fieldName        is key to {@code errorLog } map
     * @param errorMessage     describes the cause validation fail
     */
    public ValidationResult(ValidationStatus validationStatus, String fieldName, String errorMessage) {
        this.validationStatus = validationStatus;
        errorLog.put(fieldName, errorMessage);
    }

    /**
     * Appends information on new errors occured while validation process
     *
     * @param validationResult is the result of last validation
     */
    public void append(ValidationResult validationResult) {
        if (validationResult.validationStatus == ValidationStatus.FAILED) {
            validationStatus = ValidationStatus.FAILED;
        }
        errorLog.putAll(validationResult.errorLog);
    }

    /**
     * Adds values to {@code errorLog}
     *
     * @param fieldName    is key to {@code errorLog map}, it represents the input parameter
     *                     where validation error occured
     * @param errorMessage is the cause of occured error
     */
    public void addError(String fieldName, String errorMessage) {
        errorLog.put(fieldName, errorMessage);
        validationStatus = ValidationStatus.FAILED;
    }

    /**
     * Transforms validation errors with  messages belonging to them
     * into URL parameters with corresponding values
     *
     * @return URL string
     */
    public String toURL() {
        return errorLog.entrySet().stream()
                .map(entry -> entry.getKey().concat("=").concat(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
     * Returns ValidationStatus object
     *
     * @return ValidationStatus object
     */
    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

}

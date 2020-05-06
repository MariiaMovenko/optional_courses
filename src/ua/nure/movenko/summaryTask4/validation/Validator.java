package ua.nure.movenko.summaryTask4.validation;

import javax.servlet.http.HttpServletRequest;

/**
 * Validates request data
 *
 * @author M.Movenko
 */
public interface Validator {

    /**
     * Executes validators' chain
     *
     * @param request is {@code HttpRequest}
     * @return {@code ValidationResult} object
     */
    ValidationResult validate(HttpServletRequest request);

    /**
     * Sets the validator to be executed next
     *
     * @param validator is nex executed validator
     */
    void next(Validator validator);

}

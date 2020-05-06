package ua.nure.movenko.summaryTask4.validation;

import ua.nure.movenko.summaryTask4.enums.ValidationStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * An abstract AbstractValidator class  based  on implementation of the {@code Validator} interface.
 *
 * @ author M.Movenko
 */
public abstract class AbstractValidator implements Validator {

    private Validator next;

    AbstractValidator() {
    }

    /**
     * Makes validators' chain to be executed and defines either this chain should be continued or not
     *
     * @param request is {@code HttpServletRequest}
     * @return {@code ValidationResult} object
     */
    @Override
    public ValidationResult validate(HttpServletRequest request) {
        ValidationResult result = new ValidationResult(ValidationStatus.SUCCESS);
        boolean shouldContinue = executeValidation(request, result);
        return shouldContinue ? executeNext(result, request) : result;
    }

    /**
     * Executes the validation logic and defines  current validation  result
     *
     * @param request contains  parameters to validate
     * @param result  parameter for occured errors to be added to
     * @return {@code true} if the validation chain should be continued after executing of this method
     */
    protected abstract boolean executeValidation(HttpServletRequest request, ValidationResult result);

    /**
     * Sets the validator to be executed next
     *
     * @param validator is nex executed validator
     */
    @Override
    public void next(Validator validator) {
        next = validator;
    }

    private ValidationResult executeNext(ValidationResult result, HttpServletRequest request) {
        if (next != null) {
            result.append(next.validate(request));
        }
        return result;
    }
}

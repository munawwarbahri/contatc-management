package contactmanagement.restful.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public void validate(Object request) {
        Set<ConstraintViolation<Object>> consctraintViolations = validator.validate(request);
        if (consctraintViolations.size() != 0) {
            //error
            throw new ConstraintViolationException(consctraintViolations);
        }
    }
}

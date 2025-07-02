package indradwi_restfull.service;

import indradwi_restfull.entity.User;
import indradwi_restfull.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import indradwi_restfull.model.RegisterUserRequest;
import indradwi_restfull.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Validator validator;

	public void register(RegisterUserRequest request) {
		Set<ConstraintViolation<RegisterUserRequest>> constraintViolations = validator.validate(request);
		if (constraintViolations.size() != 0) {
			// error
			throw new ConstraintViolationException(constraintViolations);
		}

		if (userRepository.existsById(request.getUsername())) {
			throw new ApiException("Username is already registered");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
	}

}

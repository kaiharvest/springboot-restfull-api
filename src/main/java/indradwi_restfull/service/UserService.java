package indradwi_restfull.service;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.UpdateUserRequest;
import indradwi_restfull.model.UserResponse;
import indradwi_restfull.security.BCrypt;
import jakarta.transaction.Transactional;
import indradwi_restfull.model.RegisterUserRequest;
import indradwi_restfull.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ValidationService validationService;

	@Transactional
	public void register(RegisterUserRequest request) {
		validationService.validate(request);

		if (userRepository.existsById(request.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username is already registered");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
		user.setName(request.getName());

		userRepository.save(user);
	}

	public UserResponse get(User user) {
		return UserResponse.builder()
				.username(user.getUsername())
				.name(user.getName())
				.build();
	}

	public UserResponse update(User user, UpdateUserRequest request) {
		validationService.validate(request);

		log.info("REQUEST: {} ", request);

		if (Objects.nonNull(request.getName())) {
			user.setName(request.getName());
		}

		if (Objects.nonNull(request.getPassword())) {
			user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
		}

		userRepository.save(user);

		log.info("USER: {} ", user.getUsername());

		return UserResponse.builder()
				.name(user.getName())
				.username(user.getUsername())
				.build();
	}

}

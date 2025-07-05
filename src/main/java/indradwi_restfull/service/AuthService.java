package indradwi_restfull.service;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.LoginUserRequest;
import indradwi_restfull.model.TokenResponse;
import indradwi_restfull.repository.UserRepository;
import indradwi_restfull.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ValidationService validationService;

	@Transactional
	public TokenResponse login(LoginUserRequest request) {
		validationService.validate(request);

		User user = userRepository.findById(request.getUsername())
				.orElseThrow(() ->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

		if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
			// sukses
			user.setToken(UUID.randomUUID().toString());
			user.setTokenExpireAt(next30Days());
			userRepository.save(user);

			return TokenResponse.builder()
					.token(user.getToken())
					.expiredAt(user.getTokenExpireAt())
					.build();

		} else {
			// gagal
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
		}
	}

	private Long next30Days() {
		return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
	}

}

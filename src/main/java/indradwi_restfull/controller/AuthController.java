package indradwi_restfull.controller;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.LoginUserRequest;
import indradwi_restfull.model.TokenResponse;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping(
			path = "/api/auth/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)

	private WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
		TokenResponse tokenResponse = authService.login(request);
		return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
	}

	@DeleteMapping(
			path = "/api/auth/logout",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<String> logout(User user) {
		authService.logout(user);
		return WebResponse.<String>builder().data("OK").build();
	}

}

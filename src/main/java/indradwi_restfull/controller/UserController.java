package indradwi_restfull.controller;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.RegisterUserRequest;
import indradwi_restfull.model.UpdateUserRequest;
import indradwi_restfull.model.UserResponse;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(
			path = "/api/user",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
		userService.register(request);
		return  WebResponse.<String>builder().data("OK").build();
	}

	@GetMapping(
			path = "/api/user/current",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<UserResponse> get(User user) {
		UserResponse userResponse = userService.get(user);
		return  WebResponse.<UserResponse>builder().data(userResponse).build();
	}

	@PatchMapping(
			path = "/api/user/current",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
		UserResponse userResponse = userService.update(user, request);
		return  WebResponse.<UserResponse>builder().data(userResponse).build();
	}

}

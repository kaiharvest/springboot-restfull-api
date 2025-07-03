package indradwi_restfull.controller;

import indradwi_restfull.model.RegisterUserRequest;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}

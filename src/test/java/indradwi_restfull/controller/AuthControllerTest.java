package indradwi_restfull.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import indradwi_restfull.entity.User;
import indradwi_restfull.model.LoginUserRequest;
import indradwi_restfull.model.TokenResponse;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.repository.UserRepository;
import indradwi_restfull.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	void loginFailed() throws Exception {
		LoginUserRequest request = new LoginUserRequest();
		request.setUsername("test");
		request.setPassword("test");

		mockMvc.perform(
				post("/api/auth/login")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isUnauthorized()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void loginWithWrongPassword() throws Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
		userRepository.save(user);

		LoginUserRequest request = new LoginUserRequest();
		request.setUsername("test");
		request.setPassword("salah");

		mockMvc.perform(
				post("/api/auth/login")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isUnauthorized()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void loginSuccess() throws Exception {
		User user = new User();
		user.setName("test");
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
		userRepository.save(user);

		LoginUserRequest request = new LoginUserRequest();
		request.setUsername("test");
		request.setPassword("test");

		mockMvc.perform(
				post("/api/auth/login")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<TokenResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);
			assertNull(response.getErrors());
			assertNotNull(response.getData().getToken());
			assertNotNull(response.getData().getExpiredAt());

			User userDb = userRepository.findById("test").orElse(null);
			assertNotNull(userDb);
			assertEquals(userDb.getToken(), response.getData().getToken());
			assertEquals(userDb.getTokenExpireAt(), response.getData().getExpiredAt());
		});
	}

}

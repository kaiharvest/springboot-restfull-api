package indradwi_restfull.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import indradwi_restfull.entity.User;
import indradwi_restfull.model.RegisterUserRequest;
import indradwi_restfull.model.UpdateUserRequest;
import indradwi_restfull.model.UserResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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
	void testRegisterSuccess() throws Exception {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setUsername("test");
		request.setPassword("rahasia");
		request.setName("Test");

		mockMvc.perform(
				post("/api/user")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);

			assertEquals("OK", response.getData());
		});
	}

	@Test
	void testRegisterBadRequest() throws Exception {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setUsername("");
		request.setPassword("");
		request.setName("");

		mockMvc.perform(
				post("/api/user")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isBadRequest()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);

			assertNotNull(response.getErrors());
		});
	}

	@Test
	void testRegisterDuplication() throws Exception {
		User user = new User();
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
		user.setName("Test");
		userRepository.saveAndFlush(user);

		RegisterUserRequest request = new RegisterUserRequest();
		request.setUsername("test");
		request.setPassword("rahasia");
		request.setName("Tes");

		mockMvc.perform(
				post("/api/user")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
		).andExpectAll(
				status().isBadRequest()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);

			assertNotNull(response.getErrors());
		});
	}

	@Test
	void getUserUnauthorized() throws Exception {
		mockMvc.perform(
				get("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "notfound")
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
	void getUserUnauthorizedTokenNotSend() throws Exception {
		mockMvc.perform(
				get("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
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
	void getUserSuccess() throws Exception {
		User user = new User();
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
		user.setName("test");
		user.setToken("test-token-123");
		user.setTokenExpireAt(System.currentTimeMillis() + 1000000L);
		userRepository.saveAndFlush(user);

		mockMvc.perform(
				get("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test-token-123")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<UserResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);

			assertNull(response.getErrors());
			assertEquals("test", response.getData().getUsername());
			assertEquals("test", response.getData().getName());
		});
	}

	@Test
	void getUserTokenExpired() throws Exception {
		User user = new User();
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
		user.setName("test");
		user.setToken("test-token-123");
		user.setTokenExpireAt(System.currentTimeMillis() - 1000000);
		userRepository.saveAndFlush(user);

		mockMvc.perform(
				get("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test-token-123")
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
	void updateUserWithInvalidToken() throws Exception {
		UpdateUserRequest request = new UpdateUserRequest();

		mockMvc.perform(
				patch("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
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
	void updateUserSuccess() throws Exception {
		User user = new User();
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
		user.setName("test");
		user.setToken("test-token-123");
		user.setTokenExpireAt(System.currentTimeMillis() + 1000000000L);
		userRepository.saveAndFlush(user);

		UpdateUserRequest request = new UpdateUserRequest();
		request.setName("Indra");
		request.setPassword("indra12345");

		mockMvc.perform(
				patch("/api/user/current")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
						.header("X-API-TOKEN", "test-token-123")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<UserResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(), new TypeReference<>() {}
			);

			assertNull(response.getErrors());
			assertEquals("Indra", response.getData().getName());
			assertEquals("test", response.getData().getUsername());

			User userDb = userRepository.findById("test").orElse(null);
			assertNotNull(userDb);
			assertTrue(BCrypt.checkpw("indra12345", userDb.getPassword()));
		});
	}


}

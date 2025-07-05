package indradwi_restfull.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import indradwi_restfull.entity.Contact;
import indradwi_restfull.entity.User;
import indradwi_restfull.model.ContactResponse;
import indradwi_restfull.model.CreateContactRequest;
import indradwi_restfull.model.UpdateContactRequest;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.repository.ContactRepository;
import indradwi_restfull.repository.UserRepository;
import indradwi_restfull.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		contactRepository.deleteAll();
		userRepository.deleteAll();

		User user = new User();
		user.setUsername("test");
		user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
		user.setName("test");
		user.setToken("test");
		user.setTokenExpireAt(System.currentTimeMillis() + 1000000L);

		userRepository.save(user);
	}

	@Test
	void createContactBadRequest() throws Exception {
		CreateContactRequest request = new CreateContactRequest();
		request.setFirstName("");
		request.setEmail("salah");

		mockMvc.perform(
				post("/api/contacts")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isBadRequest()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<String>>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void createContactSuccess() throws Exception {
		CreateContactRequest request = new CreateContactRequest();
		request.setFirstName("Indra");
		request.setLastName("Dwi");
		request.setEmail("indra@example.com");
		request.setPhone("09122323232");

		mockMvc.perform(
				post("/api/contacts")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<ContactResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<ContactResponse>>() {}
			);
			assertNull(response.getErrors());
			assertEquals("Indra", response.getData().getFirstName());
			assertEquals("Dwi", response.getData().getLastName());
			assertEquals("indra@example.com", response.getData().getEmail());
			assertEquals("09122323232", response.getData().getPhone());

			assertTrue(contactRepository.existsById(response.getData().getId()));
		});
	}

	@Test
	void getContactNotFound() throws Exception {
		mockMvc.perform(
				get("/api/contacts/unknown-id")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isNotFound()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<String>>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void getContactSuccess() throws Exception {
		User user = userRepository.findById("test").orElseThrow();

		Contact contact = new Contact();
		contact.setId(UUID.randomUUID().toString());
		contact.setUser(user);
		contact.setFirstName("Indra");
		contact.setLastName("Dwi");
		contact.setEmail("indra@example.com");
		contact.setPhone("09122323232");
		contactRepository.save(contact);

		mockMvc.perform(
				get("/api/contacts/" + contact.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<ContactResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<>() {}
			);
			assertNull(response.getErrors());
			assertEquals(contact.getId(), response.getData().getId());
			assertEquals(contact.getFirstName(), response.getData().getFirstName());
			assertEquals(contact.getLastName(), response.getData().getLastName());
			assertEquals(contact.getEmail(), response.getData().getEmail());
			assertEquals(contact.getPhone(), response.getData().getPhone());
		});
	}

	@Test
	void updateContactBadRequest() throws Exception {
		UpdateContactRequest request = new UpdateContactRequest();
		request.setFirstName("");
		request.setEmail("salah");

		mockMvc.perform(
				put("/api/contacts/1234")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isBadRequest()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<String>>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void updateContactSuccess() throws Exception {
		User user = userRepository.findById("test").orElseThrow();

		Contact contact = new Contact();
		contact.setId(UUID.randomUUID().toString());
		contact.setUser(user);
		contact.setFirstName("Indra");
		contact.setLastName("Dwi");
		contact.setEmail("indra@example.com");
		contact.setPhone("09122323232");
		contactRepository.save(contact);

		UpdateContactRequest request = new UpdateContactRequest();
		request.setFirstName("Budi");
		request.setLastName("Nugraha");
		request.setEmail("budi@example.com");
		request.setPhone("232323232");

		mockMvc.perform(
				put("/api/contacts/" + contact.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(request))
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<ContactResponse> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<ContactResponse>>() {}
			);
			assertNull(response.getErrors());
			assertEquals(request.getFirstName(), response.getData().getFirstName());
			assertEquals(request.getLastName(), response.getData().getLastName());
			assertEquals(request.getEmail(), response.getData().getEmail());
			assertEquals(request.getPhone(), response.getData().getPhone());

			assertTrue(contactRepository.existsById(response.getData().getId()));
		});
	}

	@Test
	void deleteContactNotFound() throws Exception {
		mockMvc.perform(
				delete("/api/contacts/unknown-id")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isNotFound()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<WebResponse<String>>() {}
			);
			assertNotNull(response.getErrors());
		});
	}

	@Test
	void deleteContactSuccess() throws Exception {
		User user = userRepository.findById("test").orElseThrow();

		Contact contact = new Contact();
		contact.setId(UUID.randomUUID().toString());
		contact.setUser(user);
		contact.setFirstName("Indra");
		contact.setLastName("Dwi");
		contact.setEmail("indra@example.com");
		contact.setPhone("09122323232");
		contactRepository.save(contact);

		mockMvc.perform(
				delete("/api/contacts/" + contact.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header("X-API-TOKEN", "test")
		).andExpectAll(
				status().isOk()
		).andDo(result -> {
			WebResponse<String> response = objectMapper.readValue(
					result.getResponse().getContentAsString(),
					new TypeReference<>() {}
			);
			assertNull(response.getErrors());
			assertEquals("Ok", response.getData());
		});
	}

}

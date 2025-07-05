package indradwi_restfull.controller;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.ContactResponse;
import indradwi_restfull.model.CreateContactRequest;
import indradwi_restfull.model.UpdateContactRequest;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

	@Autowired
	private ContactService contactService;

	@PostMapping(
			path = "/api/contacts",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<ContactResponse> create(User user,
	                                           @RequestBody CreateContactRequest request) {
		ContactResponse contactResponse = contactService.create(user, request);
		return WebResponse.<ContactResponse>builder().data(contactResponse).build();
	}

	@GetMapping(
			path = "/api/contacts/{contactId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<ContactResponse> get(User user,
	                                        @PathVariable("contactId") String id) {
		ContactResponse contactResponse = contactService.get(user, id);
		return WebResponse.<ContactResponse>builder().data(contactResponse).build();
	}

	@PutMapping(
			path = "/api/contacts/{contactId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<ContactResponse> update(User user,
	                                           @RequestBody UpdateContactRequest request,
	                                           @PathVariable("contactId") String contactId) {
		request.setId(contactId);
		ContactResponse contactResponse = contactService.update(user, request);
		return WebResponse.<ContactResponse>builder().data(contactResponse).build();
	}

	@DeleteMapping(
			path = "/api/contacts/{contactId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<String> delete(User user, @PathVariable("contactId") String id) {
		contactService.delete(user, id);
		return WebResponse.<String>builder().data("Ok").build();
	}
}

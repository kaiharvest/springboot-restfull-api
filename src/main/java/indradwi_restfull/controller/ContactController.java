package indradwi_restfull.controller;

import indradwi_restfull.entity.User;
import indradwi_restfull.model.ContactResponse;
import indradwi_restfull.model.CreateContactRequest;
import indradwi_restfull.model.WebResponse;
import indradwi_restfull.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

	@Autowired
	private ContactService contactService;

	@PostMapping(
			path = "/api/contacts",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
		ContactResponse contactResponse = contactService.create(user, request);
		return WebResponse.<ContactResponse>builder().data(contactResponse).build();
	}

}

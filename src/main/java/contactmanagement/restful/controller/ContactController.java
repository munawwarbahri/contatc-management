package contactmanagement.restful.controller;

import contactmanagement.restful.entity.User;
import contactmanagement.restful.model.ContactResponse;
import contactmanagement.restful.model.CreateContactRequest;
import contactmanagement.restful.model.WebResponse;
import contactmanagement.restful.service.ContactService;
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


    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) { //Create(User user, Contact contact) menerangkan user harus login
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String contactId) {
        ContactResponse contactResponse = contactService.get(user, contactId);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
}

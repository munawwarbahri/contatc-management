package contactmanagement.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import contactmanagement.restful.entity.Contact;
import contactmanagement.restful.entity.User;
import contactmanagement.restful.model.ContactResponse;
import contactmanagement.restful.model.CreateContactRequest;
import contactmanagement.restful.model.UpdateContactRequest;
import contactmanagement.restful.model.WebResponse;
import contactmanagement.restful.repository.ContactRepository;
import contactmanagement.restful.repository.UserRepository;
import contactmanagement.restful.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private ContactRepository contactRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        objectMapper = new ObjectMapper();

        User  user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000);
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Bahri");
        request.setLastName("Munawwar");
        request.setEmail("bahri@example.com");
        request.setPhone("123456789");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNull(response.getErrors());
            assertEquals("Bahri", response.getData().getFirstName());
            assertEquals("Munawwar", response.getData().getLastName());
            assertEquals("bahri@example.com", response.getData().getEmail());
            assertEquals("123456789", response.getData().getPhone());

            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getContactNotFound() throws Exception {
         mockMvc.perform(
                get("/api/contacts/41434241")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("Bahri");
        contact.setLastName("Munawwar");
        contact.setEmail("bahri@example.com");
        contact.setPhone("123456789");
        contactRepository.save(contact);


        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("Bahri");
        contact.setLastName("Munawwar");
        contact.setEmail("bahri@example.com");
        contact.setPhone("123456789");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Wahyu");
        request.setLastName("Widodo");
        request.setEmail("wahyu@example.com");
        request.setPhone("912345678");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

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
                delete("/api/contacts/41434241")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteontactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("Bahri");
        contact.setLastName("Munawwar");
        contact.setEmail("bahri@example.com");
        contact.setPhone("123456789");
        contactRepository.save(contact);


        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals("OK",  response.getData());
        });
    }

    @Test
    void searchNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(0,  response.getData().size());
            assertEquals(0,  response.getPaging().getTotalPage());
            assertEquals(0,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });
    }

    @Test
    void searchSucess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setUser(user);
            contact.setFirstName("Bahri" + i);
            contact.setLastName("Munawwar");
            contact.setEmail("bahri@example.com");
            contact.setPhone("123456789");
            contactRepository.save(contact);

        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Bahri")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(10,  response.getData().size());
            assertEquals(10,  response.getPaging().getTotalPage());
            assertEquals(0,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Munawwar")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(10,  response.getData().size());
            assertEquals(10,  response.getPaging().getTotalPage());
            assertEquals(0,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "example.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(10,  response.getData().size());
            assertEquals(10,  response.getPaging().getTotalPage());
            assertEquals(0,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "12345")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(10,  response.getData().size());
            assertEquals(10,  response.getPaging().getTotalPage());
            assertEquals(0,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "12345")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            assertNull(response.getErrors());
            assertEquals(0,  response.getData().size());
            assertEquals(10,  response.getPaging().getTotalPage());
            assertEquals(1000,  response.getPaging().getCurrentPage());
            assertEquals(10,  response.getPaging().getSize());

        });
    }
}
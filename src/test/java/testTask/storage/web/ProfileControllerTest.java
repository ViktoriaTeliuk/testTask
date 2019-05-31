package testTask.storage.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import rest.files.model.User;
import rest.files.service.UserService;
import rest.files.to.ResponseAnswer;
import rest.files.to.ResponseTo;
import rest.files.web.ProfileController;
import rest.files.web.json.JsonUtil;
import testTask.storage.TestData;
import testTask.storage.util.TestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testTask.storage.TestData.RESPONSE_SUCCESS;
import static testTask.storage.TestData.USER_1;
import static testTask.storage.util.TestUtil.getContent;
import static testTask.storage.util.TestUtil.userHttpBasic;

class ProfileControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileController.REST_URL + '/';

    @Autowired
    private UserService service;

 /*   @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
*/
    @Test
    void testRegister() throws Exception {
        User created = new User(null, "newName", "newemail@ya.ru", "newPassword");

        ResultActions action = mockMvc.perform(post(REST_URL + "/signup").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created)))
                .andDo(print())
                .andExpect(status().isCreated());
        ResponseTo returned = TestUtil.readFromJson(action, ResponseTo.class);

        TestData.assertMatch(returned.getStatus(), ResponseAnswer.SUCCESS);
        TestData.assertMatch(service.getByEmail("newemail@ya.ru"), created, "id", "registered", "password");
    }

    @Test
    void testRegisterWithExistedMail() throws Exception {
        User created = new User(null, "newName", "user1@yandex.ru", "newPassword");

        ResultActions action = mockMvc.perform(post(REST_URL + "/signup").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        ResponseTo returned = TestUtil.readFromJson(action, ResponseTo.class);

        TestData.assertMatch(returned.getStatus(), ResponseAnswer.ERROR);
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post(REST_URL + "login")
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), ResponseTo.class), RESPONSE_SUCCESS, "expiresAt", "token"));
    }
}
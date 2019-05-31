package testTask.storage.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rest.files.model.UserFile;
import rest.files.service.StorageService;
import rest.files.to.ResponseTo;
import rest.files.web.ProfileController;
import rest.files.web.StorageController;
import rest.files.web.json.JsonUtil;
import testTask.storage.TestData;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static testTask.storage.TestData.*;
import static testTask.storage.util.TestUtil.getContent;

public class StorageControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileController.REST_URL + '/';

    @Autowired
    private StorageService service;

    @BeforeEach
    void setHeaderValue() {
        super.getHeaderValue();
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL + "list")
                .header("Authorization", basicHeaderValue))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), ResponseTo.class), RESPONSE_USER_1_FILES));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_1_FILE1.getName())
                .header("Authorization", basicHeaderValue))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), ResponseTo.class), RESPONSE_SUCCESS));

        assertMatch(service.getAll(USER_ID_1), List.of(USER_1_FILE2));
    }

    @Test
    void testGetMetaOnly() throws Exception {
        mockMvc.perform(get(REST_URL + USER_1_FILE1.getName())
                .header("Authorization", basicHeaderValue))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), UserFile.class), USER_1_FILE1, "id", "userId")); //because these fields are absent in json
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + USER_1_FILE1.getName())
                .param("alt", "media")
                .header("Authorization", basicHeaderValue))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().stringValues(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + USER_1_FILE1.getPath()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType(USER_1_FILE1.getContentType())))
                .andExpect(content().bytes(Files.readAllBytes(Path.of(StorageController.ROOT_DIRECTORY, USER_1_FILE1.getPath()))));
    }

    @Test
    void testUpload() throws Exception {
        FileInputStream fis = new FileInputStream(StorageController.ROOT_DIRECTORY + TestData.USER_1_FILE_NEW_PATH);
        MockMultipartFile multipartFile = new MockMultipartFile("file", USER_1_FILE_NEW_PATH, MediaType.TEXT_PLAIN_VALUE, fis);

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "newFileBoundary");
        MediaType mediaType = new MediaType("multipart", "form-related", contentTypeParams);

        mockMvc.perform(MockMvcRequestBuilders.multipart(REST_URL + "upload")
                .file(multipartFile)
                .header("Authorization", basicHeaderValue)
                .characterEncoding("UTF-8")
                .contentType(mediaType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), ResponseTo.class), RESPONSE_USER_NEW_FILE));

        TestData.assertMatch(service.get(USER_ID_1, USER_1_FILE_NEW_NAME).getPath(), USER_1_FILE_NEW_PATH);
    }

    /*
POST /storage/find?name="*.jpg"
Results: {"message": "success", "query" = "*.jpg", "objects":["path":"/testLocation/MyFilename.jpg", "path":"/testLocation/MyFilename.jpg"]}*/
    @Test
    void testFindWildcard() throws Exception {
        mockMvc.perform(post(REST_URL + "find")
                .param("filter", FILE_SEARCH_MASK)
                .header("Authorization", basicHeaderValue))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> TestData.assertMatch(JsonUtil.readValue(getContent(result), ResponseTo.class), RESPONSE_USER_1_FILES_PATH_ONLY));

    }

}

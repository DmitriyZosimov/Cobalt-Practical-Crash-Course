package com.cobaltcourse.web;


import com.cobaltcourse.web.config.WebConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WebConfiguration.class)
public class MainControllerMockito implements PathForTest{

    private static final String PATH = PathForTest.getPath();

    @Autowired
    private MainController mainController;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private File file;

    //TODO: add logging
    @BeforeClass
    public static void setupTestFolderAndFile() throws IOException {
        File folder = new File("src/test/resource/test dir with spaces");
        if(!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File("src/test/resource/test dir with spaces/test.txt");
        if(!file.exists()) {
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(new FileWriter(file));
        pw.print("first line\n" +
                "second line\n" +
                "third line\n" +
                "the end of the test.txt");
        pw.close();
    }

    @AfterClass
    public static void deleteTestFileAndFolder() {
        File file = new File("src/test/resource/test dir with spaces/test.txt");
        if(file.exists()) {
            file.delete();
        }

        File folder = new File("src/test/resource/test dir with spaces");
        if(folder.exists()) {
            folder.delete();
        }
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    //main method
    @Test
    public void shouldReturnListDirectories() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        List<String> links = mapper.readValue(response.getContentAsString(), new TypeReference<List<String>>() {
        });
        Assertions.assertFalse(links.isEmpty());
    }

    //TODO: check on windows how it works
    @Test
    public void shouldReadTheFile() throws Exception {
        file = new File("src/test/resource/test dir with spaces/test.txt");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(file.getAbsolutePath() + "/")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        String text = mapper.readValue(response.getContentAsString(), new TypeReference<String>() {
        });
        Assertions.assertNotNull(text);
    }

    @Test
    public void shouldReturn404WhenFileDoesNotExist() throws Exception {
        file = new File("src/test/resource/file_not_found.txt");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(file.getAbsolutePath() + "/")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
    }

    @Test
    public void shouldReturn200WhenDirHasSpacesInName() throws Exception {
        file = new File("src/test/resource/test dir with spaces");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(file.getAbsolutePath())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
    }

    //createFileOrDir method
    @Test
    public void shouldReturn200AfterCreatingFolder() throws Exception {
        file = new File("src/test/resource/test dir with spaces");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(file.getAbsolutePath())
                .param("name", "created folder"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File createdFolder = new File("src/test/resource/test dir with spaces/created folder");
        Assertions.assertTrue(createdFolder.exists());

        createdFolder.delete();
    }

    @Test
    public void shouldReturn400WhenFolderAlreadyExists() throws Exception {
        File createdFolder = new File("src/test/resource/test dir with spaces/created folder");
        createdFolder.mkdirs();
        Assertions.assertTrue(createdFolder.exists());

        file = new File("src/test/resource/test dir with spaces");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(file.getAbsolutePath())
                .param("name", "created folder"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);

        createdFolder.delete();
    }

    @Test
    public void shouldReturn200AfterCreatingFile() throws Exception {
        file = new File("src/test/resource/test dir with spaces");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(file.getAbsolutePath())
                .param("name", "new file.txt")
                .param("file", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File createdFile = new File("src/test/resource/test dir with spaces/new file.txt");
        Assertions.assertTrue(createdFile.exists());

        createdFile.delete();
    }

    @Test
    public void shouldReturn400WhenFileAlreadyExists() throws Exception {
        File createdFile = new File("src/test/resource/test dir with spaces/new file.txt");
        createdFile.createNewFile();
        Assertions.assertTrue(createdFile.exists());

        file = new File("src/test/resource/test dir with spaces");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(file.getAbsolutePath())
                .param("name", "new file.txt")
                .param("file", "true"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);

        createdFile.delete();
    }

    //deleteFileOrDir
    @Test
    public void shouldReturn200AfterDeletingFolder() throws Exception {
        File createdFolder = new File("src/test/resource/test dir with spaces/created folder");
        createdFolder.mkdirs();
        Assertions.assertTrue(createdFolder.exists());

        file = new File("src/test/resource/test dir with spaces/created folder");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(file.getAbsolutePath()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File deletedFolder = new File("src/test/resource/test dir with spaces/created folder");
        Assertions.assertFalse(deletedFolder.exists());
    }

    @Test
    public void shouldReturn400WhenFolderDoesNotExist() throws Exception {
        file = new File("src/test/resource/test dir with spaces/created folder");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(file.getAbsolutePath()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File deletedFolder = new File("src/test/resource/test dir with spaces/created folder");
        Assertions.assertFalse(deletedFolder.exists());
    }

    @Test
    public void shouldReturn200AfterDeletingFile() throws Exception {
        File createdFolder = new File("src/test/resource/test dir with spaces/new file.txt");
        createdFolder.createNewFile();
        Assertions.assertTrue(createdFolder.exists());

        file = new File("src/test/resource/test dir with spaces/new file.txt");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(file.getAbsolutePath()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File deletedFolder = new File("src/test/resource/test dir with spaces/new file.txt");
        Assertions.assertFalse(deletedFolder.exists());
    }

    @Test
    public void shouldReturn400WhenFileDoesNotExist() throws Exception {
        file = new File("src/test/resource/test dir with spaces/new file.txt");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(file.getAbsolutePath() + "/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();
        Assertions.assertNotNull(response);
        File deletedFolder = new File("src/test/resource/test dir with spaces/new file.txt");
        Assertions.assertFalse(deletedFolder.exists());
    }
}

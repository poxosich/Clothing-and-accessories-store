package com.example.clothingandaccessoriesstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PictureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${image.package.url}")
    private String path;

    @Test
    void getImage() throws Exception {
        File testDir = new File(path);
        if (!testDir.exists()) testDir.mkdirs();

        File image = new File(path, "test.jpg");
        try (FileOutputStream fos = new FileOutputStream(image)) {
            fos.write(new byte[]{1, 2, 3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mockMvc.perform(get("/getImage")
                        .param("picture", "test.jpg")
                        .contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }

    @Test
    void getImage_ImageIsExist() throws Exception {
        mockMvc.perform(get("/getImage")
                        .param("picture", "notfound.jpg"))
                .andExpect(content().bytes(new byte[0]));
    }
}
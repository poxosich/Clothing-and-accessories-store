package com.example.clothingandaccessoriesstore.cantroller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class PictureController {
    @Value("${image.package.url}")
    private String path;

    @ResponseBody
    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@RequestParam(name = "picture") String picture) throws IOException {
        File file = new File(path, picture);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return new byte[0];
    }
}

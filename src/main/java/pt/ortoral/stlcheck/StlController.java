package pt.ortoral.stlcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class StlController {

    public record Stl(String stl) {}

    @GetMapping("/stl")
    public Stl stl() {
        return new Stl("stl");
    }

    @PostMapping("readStl")
    public byte[] readStl(@RequestParam("file") MultipartFile file) throws IOException {
        return file.getBytes();
    }
}

package pt.ortoral.stlcheck;

import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StlController {

    @PostMapping("/stl")
    public StlReader.Stats inspect(@RequestParam MultipartFile file) throws IOException {
        try (var in = file.getInputStream()) {
            return StlReader.read(in);
        }
    }
}

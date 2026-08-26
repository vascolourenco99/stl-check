package pt.ortoral.stlcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class StlController {

    public record Stl(String stl) {}

    private final StlParserService parserService;

    public StlController(StlParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping("/stl")
    public Stl stl() {
        return new Stl("stl");
    }

    @PostMapping("/readStl")
    public StlInfo readStl(@RequestParam("file") MultipartFile file) throws IOException {
        return parserService.parse(file.getBytes());
    }
}

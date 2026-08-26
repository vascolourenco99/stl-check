package pt.ortoral.stlcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StlController {

    public record Stl(String stl) {}

    @GetMapping("/stl")
    public Stl stl() {
        return new Stl("stl");
    }
}

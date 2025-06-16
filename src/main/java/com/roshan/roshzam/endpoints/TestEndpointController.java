package com.roshan.roshzam.endpoints;

import com.roshan.roshzam.domain.models.Greeting;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestEndpointController {
    private static final String outputTemplate = "Hello %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(outputTemplate, name));
    }

}

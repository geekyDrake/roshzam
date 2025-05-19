package com.roshan.roshzam.endpoints;

import com.roshan.roshzam.domain.models.Greeting;
import com.roshan.roshzam.domain.models.TestDbRequest;
import com.roshan.roshzam.services.JpaDatabaseService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestEndpointController {
    private static final String outputTemplate = "Hello %s!";
    private final AtomicLong counter = new AtomicLong();

    private final JpaDatabaseService databaseService;

    public TestEndpointController(final JpaDatabaseService jpaDatabaseService) {
        this.databaseService = jpaDatabaseService;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(outputTemplate, name));
    }

    @PostMapping("/addTestDbEntry")
    public String addTestDbEntry(@RequestBody TestDbRequest request) {
        return databaseService.addTestRecord(request.inputValue());
    }

    @GetMapping("/getTestDbEntry")
    public String getTestDbEntry(){
        return databaseService.getAllTestRecords();
    }

}

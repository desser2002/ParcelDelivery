package org.dzianisbova.parceldelivery.loadgenerator.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.loadgenerator.application.LoadGeneratorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/load")
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
@RequiredArgsConstructor
class LoadController {
    private final LoadGeneratorService loadGeneratorService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void generateLoad(@RequestParam int count) {
        loadGeneratorService.generate(count);
    }

    @PostMapping("/generate-track")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void generateTrackLoad(@RequestParam int count) {
        loadGeneratorService.generateTrackRequests(count);
    }
}

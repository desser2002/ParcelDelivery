package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
class LoadController {
    private final LoadGeneratorService loadGeneratorService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void generateLoad(@RequestParam int count) {
        loadGeneratorService.generate(count);
    }
}

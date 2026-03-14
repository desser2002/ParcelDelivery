package org.dzianisbova.parceldelivery.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PackingScheduler {
    private final PackingOrchestrationService orchestrationService;

    @Scheduled(fixedDelayString = "${packing.scheduler.interval-ms:60000}",
            initialDelayString = "${packing.scheduler.initial-delay-ms:60000}")
    public void run() {
        orchestrationService.runPacking();
    }
}

package org.dzianisbova.parceldelivery.dispatch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DispatchScheduler {
    private final DispatchService dispatchService;

    @Scheduled(fixedDelayString = "${packing.scheduler.interval-ms:60000}",
            initialDelayString = "${packing.scheduler.initial-delay-ms:60000}")
    public void run() {
        dispatchService.dispatch();
    }
}

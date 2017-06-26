package com.cisco.oss.tools.http.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yair Ogen (yaogen) on 25/06/2017.
 */
@RestController
@Profile("server")
@Slf4j
public class ServerSimulator {

    @Value("${serversim.maxthreads:100}")
    private int maxthreads;

    private ExecutorService executorService = null;

    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(maxthreads);
    }

    @PostMapping ("")
    public DeferredResult<ResponseEntity<String>> getRoot(@RequestParam (defaultValue = "10") final int sleep, @RequestBody final String body){


        DeferredResult deferredResult = new DeferredResult(sleep*2L);

        executorService.submit(() -> {
            try {
                log.trace("sleeping for {} millis", sleep);
                TimeUnit.MILLISECONDS.sleep(sleep);
                deferredResult.setResult(body);
            } catch (InterruptedException e) {
                log.error(e.toString());
            }
        });


        return deferredResult;

    }

}

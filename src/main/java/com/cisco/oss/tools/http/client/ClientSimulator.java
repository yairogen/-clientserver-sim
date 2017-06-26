package com.cisco.oss.tools.http.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yair Ogen (yaogen) on 25/06/2017.
 */
@Profile("client")
@Component
@Slf4j
public class ClientSimulator extends Thread{

    public ClientSimulator() {
        super("ClientSim");
    }

    @Value("${serversim.host:localhost}")
    private String host = "localhost";

    @Value("${serversim.port:1357}")
    private int  port = 1357;

    @Value("${serversim.sleep:10}")
    private String sleep;

    @Value("${serversim.loadwait:100}")
    private int loadwait;

    @Autowired
    private AsyncRestTemplate restTemplate = null;

    @PostConstruct
    public void init(){
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        Map<String,String> params = new HashMap<>();
        params.put("sleep",sleep);
//        final int loadwait = this.loadwait;
        while (true) {
            ListenableFuture<ResponseEntity<String>> resp = restTemplate.postForEntity("http://"+host+":"+port+"?sleep=" + sleep, new HttpEntity("Hello World"), String.class, params);
            resp.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("failed on request. error is: {}", throwable.toString());
                }

                @Override
                public void onSuccess(ResponseEntity<String> stringResponseEntity) {
                    log.debug("got response: '{}'", stringResponseEntity.getBody());
                }
            });
            try {
                TimeUnit.MILLISECONDS.sleep(loadwait);
            } catch (InterruptedException e) {
                log.trace(e.toString());
            }

        }
    }
}

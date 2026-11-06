package com.itmentorcommunityplatform.authservice.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ProfileServiceWebClientConfig {

    @Bean
    public WebClient profileServiceWebClient(ProfileServiceProperties props) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) props.connectTimeout().toMillis())
                .responseTimeout(props.readTimeout());

        return WebClient.builder()
                .baseUrl(props.url())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
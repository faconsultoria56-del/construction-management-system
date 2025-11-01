package com.management.company.service;

import com.management.company.dto.CnpjResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BrasilApiService {

    private static final Logger logger = LoggerFactory.getLogger(BrasilApiService.class);
    private final WebClient webClient;

    public BrasilApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://brasilapi.com.br/api").build();
    }

    public Mono<CnpjResponseDTO> getCnpjData(String cnpj) {
        return webClient.get()
                .uri("/cnpj/v1/{cnpj}", cnpj)
                .retrieve()
                .bodyToMono(CnpjResponseDTO.class)
                .doOnError(error -> logger.error("Error fetching CNPJ data for {}: {}", cnpj, error.getMessage()))
                .onErrorResume(error -> Mono.empty());
    }
}

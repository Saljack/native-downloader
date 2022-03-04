package cz.saljack.downloader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class DownloaderService {

  private final WebClient webClient;

  public DownloaderService(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<String> download() {
    return webClient.get().uri("http://localhost:8080/download").retrieve().bodyToMono(String.class);
  }

}

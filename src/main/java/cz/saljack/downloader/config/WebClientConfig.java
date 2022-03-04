package cz.saljack.downloader.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebClientConfig.class);

  private static final String DF = "df";

  @Value("${username}")
  private String username;

  @Value("${password}")
  private String password;

  @Bean
  WebClient webClientOAuth(WebClient.Builder webClientBuilder,
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
        authorizedClientManager);
    oauth2Client.setDefaultOAuth2AuthorizedClient(true);
    oauth2Client.setDefaultClientRegistrationId(DF);

    return webClientBuilder
        .filter(oauth2Client)
        .build();
  }

  @Bean
  public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {

    ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
        .builder()
        .password()
        .build();

    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    authorizedClientManager
        .setContextAttributesMapper(req -> Mono.just(Map.of(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME,
            username, OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password)));
    return authorizedClientManager;
  }
}

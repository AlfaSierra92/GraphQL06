package se.magnus.microservices.composite.product.configuration;

import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.api.core.user.User;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${api.url}")
    private String apiUrl;

    //@Value("${api.auth.header}")
    //private String authHeader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Aggiungi l'intestazione di autenticazione
        // headers.set("Authorization", authHeader);
        // Puoi anche aggiungere altre intestazioni se necessario
        String admin = "user";
        String password = "password";
        String credentials = admin + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedCredentials);


        try {
            // Creare una richiesta HttpEntity con intestazioni
            HttpEntity<String> request = new HttpEntity<>(headers);

            // Fare una chiamata GET alla tua API
            ResponseEntity<User> response = restTemplate.exchange(
                    apiUrl + "/auth/" + username,
                    HttpMethod.GET,
                    request,
                    User.class
            );

            // Verifica se la risposta contiene i dettagli dell'utente
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                User userResponse = response.getBody();

                // Costruisci e restituisci l'oggetto UserDetails basato sulla risposta
                return org.springframework.security.core.userdetails.User.withUsername(userResponse.getName())
                        .password(userResponse.getPassword())
                        //.roles(userResponse.getRoles().toArray(new String[0]))
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            } else {
                throw new RuntimeException("Error communicating with the authentication service", ex);
            }
        }
    }
}

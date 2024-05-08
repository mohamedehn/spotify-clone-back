package fr.ehn.spotifycloneback.usercontext.presentation;

import fr.ehn.spotifycloneback.usercontext.ReadUserDTO;
import fr.ehn.spotifycloneback.usercontext.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthResource {

    private final UserService userService;

    private final ClientRegistration registration;

    public AuthResource(UserService userService, ClientRegistrationRepository registrations) {
        this.registration = registrations.findByRegistrationId("okta");
        this.userService = userService;
    }

    //methode pour récupérer les infos de l'utilisateur connecté
    @GetMapping("/get-authenticated-user")
    public ResponseEntity<ReadUserDTO> getAuthenticatedUser(@AuthenticationPrincipal OAuth2User user) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            userService.syncWithIdp(user);
            ReadUserDTO userFromAuthentication = userService.getAuthenticatedUserFromSecurityContext();
            return ResponseEntity.ok().body(userFromAuthentication);
        }
    }

    //methode pour deconnecter un user
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String issuerUri = registration.getProviderDetails().getIssuerUri();
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issuerUri, registration.getClientId(), originUrl};
        String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl));
    }
}

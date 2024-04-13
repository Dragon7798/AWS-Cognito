package com.aws.cognito.serviceImpl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.aws.cognito.repository.UserRepository;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

@Service
public class CognitoService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.clientSecrete}")
    private String clientSecret;

    @Value("${aws.cognito.scope}")
    private String scope;

    @Value("${aws.cognito.url}")
    private String issuerUri;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Autowired
    private AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Autowired
    private UserRepository userRepository;

    public static String calculateSecretHash(String clientId, String clientSecret, String userName) {
        try {
            final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

            byte[] secretBytes = clientSecret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec signingKey = new SecretKeySpec(secretBytes, HMAC_SHA256_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            byte[] userNameBytes = userName.getBytes(StandardCharsets.UTF_8);
            byte[] clientIdBytes = clientId.getBytes(StandardCharsets.UTF_8);

            mac.update(userNameBytes);
            mac.update(clientIdBytes);

            byte[] rawHmac = mac.doFinal();

            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error calculating secret hash", e);
        }
    }

    public JsonObject registerUser(String username, String email, String password, String number) {
        // Set up the AWS Cognito registration request
        JsonObject responseObject = new JsonObject();
        String secretVal = calculateSecretHash(clientId, clientSecret, username);
        SignUpRequest signUpRequest = new SignUpRequest().withClientId(clientId)
                .withUsername(username)
                .withPassword(password)
                .withUserAttributes(new AttributeType().withName("phone_number").withValue(number), new AttributeType().withName("birthdate").withValue("1999-02-08"), new AttributeType().withName("name").withValue(username), new AttributeType().withName("email").withValue(""));

        SignUpResult signUpResponse = cognitoIdentityProvider.signUp(signUpRequest);

        if (signUpResponse.getUserConfirmed()) {
            responseObject = loginUser(username, password);
        } else {
            responseObject.addProperty("status", false);
            responseObject.addProperty("msg", "User not verified");
        }
        return responseObject;
    }

    public JsonObject loginUser(String username, String password) {
        JsonObject responseObject = new JsonObject();

        String secretVal = calculateSecretHash(clientId, clientSecret, username);

            InitiateAuthRequest authRequest = new InitiateAuthRequest().withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH).withClientId(clientId).withAuthParameters(Map.of("USERNAME", username, "PASSWORD", password));

        InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
        AuthenticationResultType authResponse = authResult.getAuthenticationResult();

        // TODO if token expires call refreshAccessToken(String refreshToken); if keep me signed in is enabled

        String accessToken = authResponse.getAccessToken();
        String idToken = authResponse.getIdToken();
        String refreshToken = authResponse.getRefreshToken();

/*
            AuthenticationResultType authResponse2 = refreshAccessToken(refreshToken);
            LOGGER.info("Response 2 : {}", authResponse2.getAccessToken(), authResponse2.getRefreshToken());
*/

        responseObject.addProperty("status", true);
        responseObject.addProperty("accessToken", accessToken);
        responseObject.addProperty("idToken", idToken);
        responseObject.addProperty("refreshToken", refreshToken);

        return responseObject;
    }

    private AuthenticationResultType refreshAccessToken(String refreshToken) {
        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
        authRequest.withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH).withClientId(clientId).withUserPoolId(userPoolId).addAuthParametersEntry("REFRESH_TOKEN", refreshToken);
        AdminInitiateAuthResult authResult = cognitoIdentityProvider.adminInitiateAuth(authRequest);
        return authResult.getAuthenticationResult();

    }


}

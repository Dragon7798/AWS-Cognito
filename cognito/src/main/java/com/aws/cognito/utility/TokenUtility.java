package com.aws.cognito.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.aws.cognito.serviceImpl.AwsCognitoRSAKeyProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenUtility {

    public static void validateJwt(String token) {

        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                //.withAudience("2qm9sgg2kh21masuas88vjc9se") // Validate your apps audience if needed
                .build();
        jwtVerifier.verify(token);
    }

    public static void main(String[] args) {
        String d = "02/02/2023";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(d);
        System.out.println(sdf.format(date));
    }
}

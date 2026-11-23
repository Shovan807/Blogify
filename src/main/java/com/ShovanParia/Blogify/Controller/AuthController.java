package com.ShovanParia.Blogify.Controller;

import com.ShovanParia.Blogify.ExceptionHandling.ApiExceptionError;
import com.ShovanParia.Blogify.Security.JwtTokenHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenHelper jwtTokenHelper ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String token = jwtTokenHelper.generateToken(userDetails);

            return Map.of("token", token);

        } catch (AuthenticationException ex) {

            throw new ApiExceptionError(
                    "Invalid username or password",true,
                    HttpStatus.UNAUTHORIZED
            );
        }

    }

    public record LoginRequest(String email, String password) {

    }


}
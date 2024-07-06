package com.coder.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "AL133Dx08UYJwL7YalPHs5uqq6hFoNX4CyGUrhNzTDN+/GehW+pZQ0sGH+Y76+Ditt4xzwcn5HTuLCIuMbyX6/7hpcyz+d1Y4pt+gpQcCkvay6gppjDOojrsehZSz/0PwR2BnaY4aK06QDoTnjwpSC6DPwQjVUf5nKYZByVK67HPPu3hxMKm+GkxUwQpswy8TxV1iLWKkSHAyNNySNxnOYF/RJq+7JwKMnaGBOxrS6OnBIRx3nzghyY+ZJ/6lCRKHgsxhuzDQvhC8UDWMK5tAjI5OIOAAtJAux9XORyFibH6bZSjMFI2hhFgGxwzAoGh1S0R+nOR3Ak5HsNALzHvjJWD95HPh1jf2vYJdpeAj78";
                
    
    public String extractUserName(String token){
        return extractCliam(token,Claims::getSubject);
    }

    public <T> T extractCliam(String token,Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object>extraCliams,UserDetails userDetails){
        return Jwts
               .builder()
               .setClaims(extraCliams)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
               .signWith(getSignInKey(),SignatureAlgorithm.HS256)
               .compact();
    }

    public boolean isTokenValid(String token ,UserDetails userDetails){
        String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiratrion(token).before(new Date());
    }

    private Date extractExpiratrion(String token) {
       return extractCliam(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
        		.setSigningKey(getSignInKey())
        		.build().parseClaimsJws(token)
        		.getBody();
    }
    
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    	return Keys.hmacShaKeyFor(keyBytes);
    }

}

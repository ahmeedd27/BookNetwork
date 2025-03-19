package com.ahmed.Spring_Security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    private  String SECRET_KEY;

    private Key getSignInKey() {
        byte[] kb= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(kb);
    }

    public String buildToken(
        Map<String , Object> claims
        , UserDetails us
        , long jwtExpiration
    ){
        var authors=us.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(us.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .claim("authorities", authors)
                .signWith(getSignInKey())
                .compact();

    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims , T> claimResolver)
    {
        final Claims claims=extractAllClaims(token);

        return claimResolver.apply(claims);
    }




    public String extractUserName(String token) {

        return extractClaim(token , Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    public boolean isTokenValid(String token , UserDetails ud){
        final String username=extractUserName(token);
        return (username.equals(ud.getUsername()) && !isTokenExpired(token));
    }



    public String generateToken(
            Map<String , Object> claims
            , UserDetails us)
    {
      return buildToken(claims , us , jwtExpiration);
    }

    public String generateToken(UserDetails us){

        return generateToken(new HashMap<>() , us);
    }


}

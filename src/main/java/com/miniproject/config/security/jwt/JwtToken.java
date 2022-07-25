package com.miniproject.config.security.jwt;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtToken extends AbstractAuthenticationToken {

    private String principal;
    private String credentials;
    private boolean authenticated;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented by
     *                    this authentication object.
     */
    public JwtToken(
          Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public JwtToken(Collection<? extends GrantedAuthority> authorities, String principal, String credentials, boolean authenticated) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }
}

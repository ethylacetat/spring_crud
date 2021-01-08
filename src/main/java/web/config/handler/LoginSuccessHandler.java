package web.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        for (GrantedAuthority authority : grantedAuthorities) {
            if (authority.getAuthority().contains("ADMIN")) {
                httpServletResponse.sendRedirect("./admin/users/");
                return;
            }
        }

        httpServletResponse.sendRedirect("./user");

    }
}
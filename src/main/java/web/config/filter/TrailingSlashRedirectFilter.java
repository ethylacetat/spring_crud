package web.config.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*@Component
@Order(Ordered.HIGHEST_PRECEDENCE)*/
public class TrailingSlashRedirectFilter extends OncePerRequestFilter {

    public TrailingSlashRedirectFilter() {
        // NO-OP
    }

    // TODO: Копипаста, проверить адекватность builder.build().getPath() и builder.toUriString()
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}

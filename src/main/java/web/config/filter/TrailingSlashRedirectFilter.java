package web.config.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
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

        if (!httpServletRequest.getRequestURI().endsWith("/")
                && httpServletRequest.getMethod().equalsIgnoreCase("get")
                // TODO: Убрать для нестандартной формы логина
                && !httpServletRequest.getRequestURI().endsWith("login")) {
            ServletUriComponentsBuilder builder =
                    ServletUriComponentsBuilder.fromRequest(httpServletRequest);
            builder.replacePath(String.format("%s/",
                    builder.build().getPath()));
            httpServletResponse.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            httpServletResponse.setHeader(HttpHeaders.LOCATION,
                    builder.toUriString());
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }
}

package rest.files.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import rest.files.service.UserService;
import rest.files.web.ExceptionInfoHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtCustomFilter extends OncePerRequestFilter {

    public static final String TOKEN_STARTS_WITH = "Bearer ";
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    private String bearerToken;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            String jwt = getJwtFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                AuthorizedUser userDetails = new AuthorizedUser(userService.get(userId.intValue()));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("Couldn't authenticate user. {}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_STARTS_WITH)) {
            log.debug(bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }

}
package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import web.config.handler.LoginSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler getSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");

        http.addFilterBefore(characterEncodingFilter, HeaderWriterFilter.class);

        http.csrf().disable().authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/logout").authenticated()
                .antMatchers("/**").authenticated()
                .and().formLogin().successHandler(new LoginSuccessHandler())
                .and().logout().logoutUrl("/logout").permitAll();
    }
}

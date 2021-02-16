package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import web.config.handler.LoginSuccessHandler;

@EnableWebSecurity
@ComponentScan("web")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler getSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Order(1)
    @Configuration
    public static class RootSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setForceEncoding(true);
            characterEncodingFilter.setEncoding("UTF-8");

            http.addFilterBefore(characterEncodingFilter, HeaderWriterFilter.class);
            /**/
            http.authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                    .antMatchers("/login").anonymous()
                    .antMatchers("/logout").authenticated()
                    .antMatchers("/**").authenticated()
                    .and().formLogin()
                    .and().logout();

            ;

        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.csrf().disable().formLogin().successHandler(new LoginSuccessHandler());

            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setForceEncoding(true);
            characterEncodingFilter.setEncoding("UTF-8");

            http.addFilterBefore(characterEncodingFilter, HeaderWriterFilter.class);
        }

    }
/*
    @Override
    public void configure(WebSecurity web) {
        super.configure(web);
        System.out.println("configure(WebSecurity)");
    }

 */
/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");


        http.addFilterBefore(characterEncodingFilter, HeaderWriterFilter.class);

        //http.csrf().disable();


        //http.csrf().disable().requestMatchers().antMatchers("/admin/**").anyRequest().

        http.csrf().disable().antMatcher("/admin/**")
                //.httpBasic().and()
                .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");

        System.out.println("configure(HttpSecurity)");

        http
                .formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(new LoginSuccessHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/login");


        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();


        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL
                .antMatchers("/hello").access("hasAnyRole('ADMIN')").anyRequest().authenticated();


    }
    */

}

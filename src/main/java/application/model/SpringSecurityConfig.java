package application.model;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws  Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("t")).roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder.encode("t")).roles("ADMIN", "USER");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable().and() // h2 access zondere deze lijn zorgt dat je terug aan de db kan
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/clubs/add").hasRole("ADMIN")
                .antMatchers("/collections/add").hasRole("ADMIN")
                .antMatchers("/clubs/update/*").hasRole("ADMIN")
                .antMatchers("/collections/update/*").hasRole("ADMIN")
                .antMatchers("/clubs/remove/*").hasRole("ADMIN")
                .antMatchers("/collections/remove/*").hasRole("ADMIN")
                .antMatchers("/clubs/overview/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/clubs/overview").hasAnyRole("USER", "ADMIN")
                .antMatchers("/collections/overview/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/collections/overview").hasAnyRole("USER", "ADMIN")
                .antMatchers("/clubs/search/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/collections/search/*").hasAnyRole("USER", "ADMIN")
                .antMatchers( "/").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/loginerror")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }
}

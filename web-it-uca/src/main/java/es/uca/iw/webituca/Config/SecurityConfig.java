package es.uca.iw.webituca.Config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import es.uca.iw.webituca.Views.Usuario.LoginView;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;*/


@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/layout//*.png")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/layout//*.svg")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/useractivation")).permitAll()
        );

        // Llamar a la configuración de Vaadin primero
        setLoginView(http, LoginView.class);

        http.formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    String redirectUrl = "/home"; // Redirección predeterminada

                    if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"))) {
                        redirectUrl = "/home-admin";
                    }
                    response.sendRedirect(redirectUrl);
                })
                .permitAll()
        );

        super.configure(http);
    }
}

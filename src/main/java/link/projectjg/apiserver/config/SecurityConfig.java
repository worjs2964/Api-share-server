package link.projectjg.apiserver.config;

import link.projectjg.apiserver.filter.JwtAuthenticationFilter;
import link.projectjg.apiserver.repository.LogoutAccessTokenRedisRepository;
import link.projectjg.apiserver.security.JwtDeniedHandler;
import link.projectjg.apiserver.security.JwtEntryPoint;
import link.projectjg.apiserver.security.JwtTokenUtil;
import link.projectjg.apiserver.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailServiceImpl userDetailsServiceImpl;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtDeniedHandler jwtDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_CHECKED_MEMBER > ROLE_MEMBER");
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/v1/members").anonymous()
                .mvcMatchers("/v1/shares").hasRole("CHECKED_MEMBER")
                .mvcMatchers("/v1/members/authentication").permitAll()
                .mvcMatchers("/v1/payment/**").permitAll()
                .anyRequest().hasRole("MEMBER");

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, userDetailsServiceImpl, logoutAccessTokenRedisRepository), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .accessDeniedHandler(jwtDeniedHandler);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v2/api-docs", "/configuration/**", "/swagger*/**",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**","/swagger/**");

        web.ignoring().antMatchers("/v1/authentication/**");
    }
}

package kr.co.sionms.demo.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

import kr.co.sionms.demo.hello.security.JwtAuthenticationFilter;
import kr.co.sionms.demo.hello.security.OAuthSuccessHandler;
import kr.co.sionms.demo.hello.security.OAuthUserServiceImpl;
import kr.co.sionms.demo.hello.security.RedirectUrlCokkieFilter;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserServiceImpl; // 우리가 만든 OAuthUserServiceImpl 추가

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler; // Success Handler 추가

    @Autowired
    private RedirectUrlCokkieFilter redirectUrlCokkieFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // WebMvcConfig에서 이미 설정 했으므로 기본 cors 설정
                .and().csrf().disable().httpBasic().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/", "/auth/**").permitAll()
                .anyRequest().authenticated().and().oauth2Login().redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().authorizationEndpoint().baseUri("/auth/authorize")
                .and().userInfoEndpoint().userService(oAuthUserServiceImpl) // OAuthUserServiceImpl를 유저 서비스로 등록;
                .and().successHandler(oAuthSuccessHandler) // Success Handler 등록
                .and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        http.addFilterBefore(redirectUrlCokkieFilter, OAuth2AuthorizationRequestRedirectFilter.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

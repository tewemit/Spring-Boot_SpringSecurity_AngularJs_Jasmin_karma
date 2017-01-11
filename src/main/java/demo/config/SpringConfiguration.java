package demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tewe on 11/25/2016.
 */
public class SpringConfiguration {


    public static final String REMEMBER_ME_KEY = "rememberme_key";

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter
    {

        /* // to add users and roles
        @Autowired
        public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("user").password("abc123").roles("USER");
            auth.inMemoryAuthentication().withUser("admin").password("root123").roles("ADMIN");
            auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");
        }*/
        private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
        public static final String REMEMBER_ME_KEY = "rememberme_key";

        public SecurityConfiguration() {
            super();
            logger.info("loading SecurityConfig ................................................ ");
        }

        @Autowired
        private UserDetailsService userDetailsService;


        @Autowired
        private RememberMeServices rememberMeServices;

        // this method overrides the default spring username and password configured on the application properties file.

        /**
         * @Description This method overrides the default spring username and password configured on the application properties file.
         *              We will be using the UserDetailsService instead.
         * @param auth
         * @throws Exception
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
            auth.inMemoryAuthentication().withUser("testadmin").password("testadmin").roles("ADMIN");
            auth.inMemoryAuthentication().withUser("testuser").password("testuser").roles("USER");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/resources/**", "/index.html", "/login.html",
                    "/partials/**", "/template/**", "/", "/error/**");
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .httpBasic()
                    .and()
                .authorizeRequests()
                    .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
                    .antMatchers("/admin/**").hasRole("admin")
                    .anyRequest().fullyAuthenticated()
                    .and()
                    //the next line, loginpage lets the backend take care of it.
                .formLogin()//.loginPage("/login")
                    .failureUrl("/login?error").permitAll()
                    .and()
                    .exceptionHandling().accessDeniedPage("/403")
                    .and()
                    /*.rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(rememberMeServices)
                    .and()*/
                .logout()//.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .and()

                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                    .csrf().csrfTokenRepository(csrfTokenRepository())
                ;


                    // would append the following to make it stateless
                    //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;
        }
    }
    /*
      tell Spring Security to expect the CSRF token in the format that Angular wants to send it back (a header called “X-XRSF-TOKEN”
      instead of the default “X-CSRF-TOKEN”). We do this by customizing the CSRF filter:
    */
    private static CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    public static class CsrfHeaderFilter extends OncePerRequestFilter
    {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                    .getName());
            if (csrf != null) {
                Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN"); //angular wants it tobe named XSRF-TOKEN
                String token = csrf.getToken();
                if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
                    cookie = new Cookie("XSRF-TOKEN", token);
                    cookie.setPath("/");
                    /*To finish the job and make it completely generic we should be careful to set the cookie path to
                    the context path of the application (instead of hard-coded to “/”), but this is good enough for the
                     application we are working on.*/
                    response.addCookie(cookie);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}

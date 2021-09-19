package com.starfire.familytree.config;

import com.starfire.familytree.filter.CustomAuthenticationFilter;
import com.starfire.familytree.filter.MyAuthenticationFailureHandler;
import com.starfire.familytree.filter.MyAuthenticationSuccessHandler;
import com.starfire.familytree.filter.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String JSESSIONID = "JSESSIONID";
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Override
    public void configure(WebSecurity web){
//        web.ignoring().anyRequest();
        web.ignoring()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/webjars/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//http.authorizeRequests().antMatchers("/**").permitAll();
http.authorizeRequests().antMatchers("/v2/api-docs",
        "/swagger-resources/**",
        "/swagger-ui.html/**",
        "/webjars/**").permitAll();
        http.cors().and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/login**","/logout","/SignUp/**","/baiduAPI/*").permitAll()
                .anyRequest().permitAll()
                .and().addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().permitAll().loginPage("/login")
                .and().logout().permitAll().logoutSuccessHandler(new MyLogoutSuccessHandler()).deleteCookies(JSESSIONID).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().csrf().disable();
    }
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter= new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        filter.setFilterProcessesUrl("/login");
        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
}

package com.leave.lams.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leave.lams.service.EmployeeDetailsService;
import com.leave.lams.util.JwtAuthenticationFilter;

/*
@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Autowired
	private EmployeeDetailsService employeeDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())	// so that csrf token is not generated
				.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/register").permitAll()
						.requestMatchers("/login", "/register").permitAll()
				        .requestMatchers(HttpMethod.PATCH, "/leaverequest/{id}/status").hasRole("ADMIN") // Corrected
				        .requestMatchers(HttpMethod.POST, "/shift/add").hasRole("ADMIN")
				        .requestMatchers("/employee/**").hasRole("ADMIN")
				        .requestMatchers(HttpMethod.PUT, "/swap/{id}/status").hasRole("ADMIN") // Corrected
				        .requestMatchers(HttpMethod.PUT, "/leaveBalances/*").hasRole("ADMIN") // This is fine for single segment
				        .requestMatchers(HttpMethod.PUT, "/leaveBalances/employee/*").hasRole("ADMIN") // This is fine for single segment
				        .requestMatchers("/attendance/add", "/attendance/{id}").hasRole("ADMIN") // This is fine
				        .requestMatchers(HttpMethod.GET,"/attendance/emp/my-attendance").hasRole("EMPLOYEE") // This is fine
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	// setting session as stateless as we use jwt
				.logout(logout->logout.disable())	// default login form and logout form
				.formLogin(form -> form.disable());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);	// adding jwtFilter before callling the USerName.. Auth..Filter so that if jwt is valid so no need to check user and password

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();	// setting auth manager which handles the auth req
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {	// setting the auth provider as we use DB to store UserDetails so DaoAuthProvider
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(employeeDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

*/


@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allow all endpoints
            )
            .formLogin(login -> login.disable()) // Disable form login
            .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic auth
            .logout(logout -> logout.disable()); // Disable logout

        return http.build();
    }
}


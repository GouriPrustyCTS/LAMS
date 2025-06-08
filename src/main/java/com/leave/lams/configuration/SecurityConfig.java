package com.leave.lams.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.leave.lams.service.EmployeeDetailsService;
import com.leave.lams.util.JwtAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Autowired
	private EmployeeDetailsService employeeDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable) // Disable CSRF
				.cors(withDefaults()) // Enable CORS using the CorsConfigurationSource bean
				.authorizeHttpRequests(auth -> auth
						// IMPORTANT: Explicitly permit all API endpoints that Angular will call
						// Put these before any more restrictive rules like .anyRequest().authenticated()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow OPTIONS for all paths (CORS preflight)
						.requestMatchers("/shift/**").permitAll() // Allow all requests to /shift/**
						.requestMatchers("/swap/**").permitAll() // Allow all requests to /swap/**

						// Other public endpoints (login, register)
						.requestMatchers("/login", "/register", "/reports/**").permitAll()


						// Authenticated access for specific endpoints (e.g., getting own details)
						.requestMatchers(HttpMethod.GET, "/employee/details").authenticated()

						// Role-based access for other endpoints
						.requestMatchers(HttpMethod.PATCH, "/leaverequest/**/status").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/shift/add").hasRole("ADMIN")
						.requestMatchers("/employee/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/swap/**/status").hasRole("ADMIN")


						// All other requests must be authenticated
						.anyRequest().authenticated()
				)
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow your Angular app's origin
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        // Allowed HTTP methods for your API
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow all headers (including Authorization header for JWT)
        configuration.setAllowedHeaders(List.of("*"));
        // Allow credentials (e.g., cookies, authorization headers)
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS config to all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
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


//
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

 */

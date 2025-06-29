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
		http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF
				.cors(withDefaults()) // Enable CORS using the CorsConfigurationSource bean
				.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/register").permitAll()
						.requestMatchers("/login", "/register", "/employee/details").permitAll()
						.requestMatchers(HttpMethod.PATCH, "/leaverequests/{id}/status").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/shift/add").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/shift/*").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/shift/*").hasRole("ADMIN")
						.requestMatchers("/employee/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/swap/{id}/status").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/leaveBalances/*").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/leaveBalances/*").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/leaveBalances/employee/*").hasRole("ADMIN")
						.requestMatchers("/attendance/add", "/attendance/{id}").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/attendance/emp/my-attendance").hasRole("EMPLOYEE")

						.anyRequest().authenticated())
				.logout(logout -> logout.disable())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	 CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Allow your Angular app's origin

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));// Allowed HTTP
																									// methods for your
																									// API

		configuration.setAllowedHeaders(List.of("*")); // Allow all headers (including Authorization header for JWT)

		configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration); // Apply this CORS config to all paths
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
// for disabling the security use this
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(login -> login.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .logout(logout -> logout.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())); // <-- This line is crucial
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Allows all headers from the client
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

 */
package br.com.cdb.bancodigitalJPA.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.cdb.bancodigitalJPA.handler.CustomAccessDeniedHandler;
import br.com.cdb.bancodigitalJPA.handler.CustomAuthenticationEntryPointHandler;
import br.com.cdb.bancodigitalJPA.security.jwt.JwtAuthFilter;
import br.com.cdb.bancodigitalJPA.security.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity // habilita a segurança na aplicação
@RequiredArgsConstructor 
public class SecurityConfig { // autenticação, autorização, e gerencia o fluxo de login
	// basicamente as configurações gerais, o que cada role pode ou não fazer, etc

	private final JwtAuthFilter jwtAuthFilter;
	
	private final UsuarioService usuarioService;
	
	private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPointHandler authenticationEntryPointHandler; //Handlers

	// a notação Bean diz mais ou menos pro Spring criar o objeto, guardar e
	// retornar ele
	// eu usei o bean pra não ter que instanciar o objeto manualmente, ai o Spring
	// já faz isso pra mim
	// é algo parecido com o Autowired pelo que eu entendi
	// e todas essas notações que tem o Bean, é essencial pra autenticação e etc,
	// mas eu não tenho 100% de controle delas, o Spring cuida disso ai
	
	
	//aqui eu defino as permissões de cada Role. Tipo um painel de controle 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
			.headers(headers -> headers.disable()) // desabilita o frame pra acessar o h2-console

			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/admin-security/**").hasRole("ADMIN")
				.requestMatchers("/clientes/admin-security/**").hasRole("ADMIN")
				.requestMatchers("/contas/admin-security/**").hasRole("ADMIN")
				.requestMatchers("/cartoes/admin-security/**").hasRole("ADMIN")
				.requestMatchers("/seguros/admin-security/**").hasRole("ADMIN")
				.requestMatchers("/cliente-security/**").hasAnyRole("ADMIN", "CLIENTE")
				.requestMatchers("/public/**", "/auth/**").permitAll()
				.anyRequest().authenticated() //se não for nada, ele só bloqueia
			)
			
			.exceptionHandling(exception -> exception
		            .authenticationEntryPoint(authenticationEntryPointHandler)
		            .accessDeniedHandler(accessDeniedHandler)
		        ) //aqui eu retorno os tratamentos de erros que eu criei no pacote Handler

			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)	//stateless pra indicar que é feito por JWT, não tem sessão 

			.authenticationProvider(authenticationProvider()) 

			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

			.build();
	}

	//admin acessa tudo com o **
	//o Admin tbm pode acessar as rotas do cliente, mas só o admin pode acessar as dele
	//só usuários com a role admin podem acessar os requests que comecem com /api/admin, com rotas livres tbm
	//addFilterBefore eu uso pra adicionar o meu filtro personalizado 
	// sessionManagement não permite que o usuário fique logado pra sempre, então ele precisa do JWT toda vez

	@Bean // ele é tipo o chefe
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // retorna o manager que vai processar login/autenticação
	} // ele vai validar se o usuário existe e se a senha tá certa, já faz isso
		// automático, pq já é configurado pelo próprio String

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // é só um criptografador de senha padrão, mas é seguro tbm
	}	//Bcrypt é um dos algoritmos mais usados pra armazenar senhas com segurança, ele não deixa a senha pura 
		//no banco de dados
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(usuarioService); 
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	} //verifica se o login/senha é valido msm

	
}

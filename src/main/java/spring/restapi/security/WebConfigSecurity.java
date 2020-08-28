package spring.restapi.security;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import spring.restapi.service.ImplementacaoUserDetailsService;

/* mapeia url,enderecos,autoriza ou bloqueia urls */
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	
	/* configuracao de requisicao http */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/* Ativando protecao contra usuarios que nao estao validados por token */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/* Ativando a permissao para acesso a pagina inicial do sistema  */
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/* redireciona o usuario dps que ele deslogar */
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/* mapeia url de logout e invalida o usuario */
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/* Filtra requisicoes de login para autenticacao */
		.and()
		.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		/* filtra demais requisicoes para verificar a presença do token jwt no header http */
		
		.addFilterBefore(new JWTApiAutenticacaoFilter(),UsernamePasswordAuthenticationFilter.class );
	}
	
	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			/* service que ira consultar o usuario no banco de dados */
			auth.userDetailsService(implementacaoUserDetailsService)
			/* padrao de codificacao de senha */
			.passwordEncoder(new BCryptPasswordEncoder());
			
		}
}

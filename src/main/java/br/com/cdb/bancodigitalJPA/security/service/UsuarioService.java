package br.com.cdb.bancodigitalJPA.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.security.model.Usuario;
import br.com.cdb.bancodigitalJPA.security.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService { // ele é responsável por buscar o usuario pelo email no
															// banco
	// e devolver pro Spring Security pra fazer a autenticação
	private final UsuarioRepository usuarioRepository;

	//tentei deixar o método em português, mas ele me força a colocar em inglês por conta da herança
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
	} // ele pega o email, busca no banco, e se não achar, lança uma exceção
	
	
	public boolean existePorEmail(String email) {
		return usuarioRepository.findByEmail(email).isPresent();
	}
	
	public Usuario save(Usuario usuario) {
	    return usuarioRepository.save(usuario);
	}

}

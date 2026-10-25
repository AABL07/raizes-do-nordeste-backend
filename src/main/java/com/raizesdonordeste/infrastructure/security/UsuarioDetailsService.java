package com.raizesdonordeste.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.raizesdonordeste.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.buscarPorLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenhaHash())
                .roles(usuario.getPerfilAcesso().getNome())
                .disabled(!usuario.getAtivo())
                .build();
    }


}
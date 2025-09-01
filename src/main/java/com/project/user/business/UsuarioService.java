package com.project.user.business;

import com.project.user.business.converter.UsuarioConverter;
import com.project.user.business.dto.EnderecoDTO;
import com.project.user.business.dto.TelefoneDTO;
import com.project.user.business.dto.UsuarioDTO;
import com.project.user.infrastructure.entity.Endereco;
import com.project.user.infrastructure.entity.Telefone;
import com.project.user.infrastructure.entity.Usuario;
import com.project.user.infrastructure.exception.ConflictException;
import com.project.user.infrastructure.exception.ResourceNotFoundException;
import com.project.user.infrastructure.repository.EnderecoRepository;
import com.project.user.infrastructure.repository.TelefoneRepository;
import com.project.user.infrastructure.repository.UsuarioRepository;
import com.project.user.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado." + email);
            }

        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado" + email))
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " +email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        // Aqui buscamos o email do usuario através do token (tirar a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // Busca os dados do usario do banco de ados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));
        // Mescla os dados que recebemos na requisicação DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // Salvos os dados do usuário convertido e depois pegou o retorno e converteu para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO((usuarioRepository.save(usuario)));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("ID não encontrado" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("ID não encontrado" + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }


}

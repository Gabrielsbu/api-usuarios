package com.electr.users.domain.services.Impl;

import com.electr.users.domain.dto.EmailDTO;
import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.enums.StatusUser;

import com.electr.users.domain.models.Role;
import com.electr.users.domain.models.Usuario;
import com.electr.users.domain.repositories.RoleRepository;
import com.electr.users.domain.repositories.UsuarioRepository;
import com.electr.users.domain.services.AvatarService;
import com.electr.users.domain.services.UsuarioService;
import com.electr.users.exceptions.AllException;
import com.electr.users.utils.UsuarioConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final AvatarService avatarService;
    private final RoleRepository roleRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private void enviarMsgEmail(EmailDTO email) throws Exception {
        this.kafkaTemplate
                .send("enviar-email", UUID.randomUUID().toString(), getString(email));
    }

    private String getString(EmailDTO email) throws Exception {
        try {
            return objectMapper.writeValueAsString(email);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String gerarSenha() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public String gerarSenhaCodificada(String senha) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.encode(senha);
    }

    @Override
    public List<UsuarioDTO> buscarTodosUsuarios(){
        List<UsuarioDTO> usuarios = usuarioConverter.toCollectionDTO(usuarioRepository.findAll());

        if(usuarios.isEmpty()){
            throw new AllException("A lista de usuários se encontra vazia", HttpStatus.NO_CONTENT);
        }

        return usuarios;

    }

    @Override
    public UsuarioDTO buscarUsuarioPorId(Long usuarioId){
        return usuarioConverter.toModelDTO(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AllException("Usuário não encontrado", HttpStatus.NOT_FOUND)));
    }

    @Override
    public UsuarioDTO salvarUsuario(String nome, String email, MultipartFile avatar, Long roleId){

        Role roleExistent = roleRepository.findById(roleId)
                .orElseThrow(() -> new AllException("Role not found", HttpStatus.NOT_FOUND));

        Usuario usuarioExistent = usuarioRepository.findUsuarioByEmail(email);

        if(usuarioExistent != null) {
            throw new AllException("Já existe um usuário com este email cadastrado, tente novamente com outro email", HttpStatus.BAD_REQUEST);
        }

        String fileDownloadUri = avatarService.createImageInServer(avatar);
        String senha = gerarSenha();
        String senhaCriptografada = gerarSenhaCodificada(senha);
        Usuario usuario = Usuario.builder().setNome(nome).setEmail(email).setSenha(senhaCriptografada).setStatus(StatusUser.ACTIVE)
                .setRole(roleExistent).setAvatar(fileDownloadUri).build();
        try {
            usuarioRepository.save(usuario);
            enviarMsgEmail(EmailDTO.builder().setEmailDestino(email).setNomeDestino(nome).setMensagem("Sua nova senha foi criada: " + senha).build());

        } catch (Exception e) {
            throw new AllException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return usuarioConverter.toModelDTO(usuario);
    }

    @Override
    public UsuarioDTO atualizarUsuario(Long usuarioId, String nome, String email, MultipartFile avatar, String senha){
        String fileDownloadUri = avatarService.createImageInServer(avatar);
        String senhaCodificada = gerarSenhaCodificada(senha);

        Usuario usuarioExistente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AllException("Usuario não encontrado", HttpStatus.NOT_FOUND));

        if(nome != null) {
            usuarioExistente.setNome(nome);
        }

        if(email != null) {
            usuarioExistente.setEmail(email);
        }

        if(avatar != null) {
            usuarioExistente.setAvatar(fileDownloadUri);
        }

        if(senha != null) {
            usuarioExistente.setSenha(senhaCodificada);
        }

        return usuarioConverter.toModelDTO(usuarioRepository.save(usuarioExistente));
    }


    @Override
    public ResponseEntity<Void> deletarUsuario(Long usuarioId){

        UsuarioDTO usuario = buscarUsuarioPorId(usuarioId);

        if( usuario == null ) {
            throw new AllException("Não existe usuário com este identificador");
        }

        usuarioRepository.deleteById(usuarioId);

        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<Resource> downloadFile(String title, HttpServletRequest request) {
        Resource resource = avatarService.loadPicture(title);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new AllException("Dont search this picture");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}

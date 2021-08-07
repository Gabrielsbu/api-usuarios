package com.electr.users.domain.services;

import com.electr.users.domain.dto.UsuarioDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface UsuarioService {

    List<UsuarioDTO> buscarTodosUsuarios();

    UsuarioDTO buscarUsuarioPorId(Long usuarioId);

    UsuarioDTO salvarUsuario(String nome, String email, MultipartFile avatar);

    UsuarioDTO atualizarUsuario(Long usuarioId, String nome, String email, MultipartFile avatar);

    ResponseEntity<Void> deletarUsuario(Long usuarioId);

    ResponseEntity<Resource> downloadFile(@PathVariable String title, HttpServletRequest request);
}

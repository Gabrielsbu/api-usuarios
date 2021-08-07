package com.electr.users.controllers;

import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/usuarios")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> buscarTodosUsuarios() {
        return usuarioService.buscarTodosUsuarios();
    }

    @GetMapping("/{usuarioId}")
    public UsuarioDTO buscarUsuarioPorId(@PathVariable Long usuarioId){
        return usuarioService.buscarUsuarioPorId(usuarioId);
    }

    @PostMapping("/salvar-usuario")
    public UsuarioDTO salvarUsuario(@RequestParam("nome") String nome,
                                    @RequestParam("email") String email,
                                    @RequestParam("avatar") MultipartFile avatar,
                                    @RequestParam("roleId") Long roleId){

        return usuarioService.salvarUsuario(nome, email, avatar, roleId);
    }

    @PutMapping("/{usuarioId}")
    public UsuarioDTO atualizarUsuario(@PathVariable Long usuarioId, @RequestParam("nome") String nome,
                                       @RequestParam("email") String email,
                                       @RequestParam("avatar") MultipartFile avatar) {

        return usuarioService.atualizarUsuario(usuarioId, nome, email, avatar);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long usuarioId){
        return usuarioService.deletarUsuario(usuarioId);
    }

    @GetMapping("/search-picture/{title:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String title, HttpServletRequest request) {
        return usuarioService.downloadFile(title, request);
    }

}
package com.electr.users.utils;

import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.models.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioConverter {

    UsuarioDTO toModelDTO(Usuario usuario);
    List<UsuarioDTO> toCollectionDTO(List<Usuario> usuarios);
}

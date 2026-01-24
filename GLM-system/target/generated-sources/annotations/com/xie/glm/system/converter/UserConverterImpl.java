package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysUser;
import com.xie.glm.system.dto.UserCreateDTO;
import com.xie.glm.system.dto.UserDTO;
import com.xie.glm.system.dto.UserUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T14:15:02+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDTO toDto(SysUser entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder<?, ?> userDTO = UserDTO.builder();

        userDTO.userId( entity.getUserId() );
        userDTO.userName( entity.getUserName() );
        userDTO.nickName( entity.getNickName() );
        userDTO.email( entity.getEmail() );
        userDTO.phonenumber( entity.getPhonenumber() );
        userDTO.sex( entity.getSex() );
        userDTO.avatar( entity.getAvatar() );
        userDTO.deptId( entity.getDeptId() );
        userDTO.status( entity.getStatus() );
        userDTO.remark( entity.getRemark() );
        userDTO.createTime( entity.getCreateTime() );
        userDTO.updateTime( entity.getUpdateTime() );
        userDTO.loginDate( entity.getLoginDate() );
        userDTO.pwdUpdateDate( entity.getPwdUpdateDate() );
        userDTO.passwordChanged( entity.getPasswordChanged() );
        userDTO.defaultPassword( entity.getDefaultPassword() );

        return userDTO.build();
    }

    @Override
    public List<UserDTO> toDtoList(List<SysUser> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( entities.size() );
        for ( SysUser sysUser : entities ) {
            list.add( toDto( sysUser ) );
        }

        return list;
    }

    @Override
    public SysUser createDtoToEntity(UserCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setDeptId( dto.getDeptId() );
        sysUser.setUserName( dto.getUserName() );
        sysUser.setNickName( dto.getNickName() );
        sysUser.setEmail( dto.getEmail() );
        sysUser.setPhonenumber( dto.getPhonenumber() );
        sysUser.setSex( dto.getSex() );
        sysUser.setAvatar( dto.getAvatar() );
        sysUser.setPassword( dto.getPassword() );
        sysUser.setStatus( dto.getStatus() );
        sysUser.setRemark( dto.getRemark() );

        return sysUser;
    }

    @Override
    public SysUser updateDtoToEntity(UserUpdateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setUserId( dto.getUserId() );
        sysUser.setDeptId( dto.getDeptId() );
        sysUser.setUserName( dto.getUserName() );
        sysUser.setNickName( dto.getNickName() );
        sysUser.setEmail( dto.getEmail() );
        sysUser.setPhonenumber( dto.getPhonenumber() );
        sysUser.setSex( dto.getSex() );
        sysUser.setAvatar( dto.getAvatar() );
        sysUser.setStatus( dto.getStatus() );
        sysUser.setRemark( dto.getRemark() );

        return sysUser;
    }

    @Override
    public void updateEntityFromDto(UserUpdateDTO dto, SysUser entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getUserId() != null ) {
            entity.setUserId( dto.getUserId() );
        }
        if ( dto.getDeptId() != null ) {
            entity.setDeptId( dto.getDeptId() );
        }
        if ( dto.getUserName() != null ) {
            entity.setUserName( dto.getUserName() );
        }
        if ( dto.getNickName() != null ) {
            entity.setNickName( dto.getNickName() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getPhonenumber() != null ) {
            entity.setPhonenumber( dto.getPhonenumber() );
        }
        if ( dto.getSex() != null ) {
            entity.setSex( dto.getSex() );
        }
        if ( dto.getAvatar() != null ) {
            entity.setAvatar( dto.getAvatar() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

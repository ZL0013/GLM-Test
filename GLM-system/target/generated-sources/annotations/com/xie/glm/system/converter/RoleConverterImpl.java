package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysRole;
import com.xie.glm.system.dto.RoleCreateDTO;
import com.xie.glm.system.dto.RoleDTO;
import com.xie.glm.system.dto.RoleUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T03:03:23+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class RoleConverterImpl implements RoleConverter {

    @Override
    public RoleDTO toDto(SysRole entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder<?, ?> roleDTO = RoleDTO.builder();

        roleDTO.roleId( entity.getRoleId() );
        roleDTO.roleName( entity.getRoleName() );
        roleDTO.roleKey( entity.getRoleKey() );
        roleDTO.roleSort( entity.getRoleSort() );
        roleDTO.dataScope( entity.getDataScope() );
        roleDTO.menuCheckStrictly( entity.getMenuCheckStrictly() );
        roleDTO.deptCheckStrictly( entity.getDeptCheckStrictly() );
        roleDTO.status( entity.getStatus() );
        roleDTO.remark( entity.getRemark() );
        roleDTO.createTime( entity.getCreateTime() );
        roleDTO.updateTime( entity.getUpdateTime() );

        return roleDTO.build();
    }

    @Override
    public List<RoleDTO> toDtoList(List<SysRole> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RoleDTO> list = new ArrayList<RoleDTO>( entities.size() );
        for ( SysRole sysRole : entities ) {
            list.add( toDto( sysRole ) );
        }

        return list;
    }

    @Override
    public SysRole createDtoToEntity(RoleCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysRole sysRole = new SysRole();

        sysRole.setRoleName( dto.getRoleName() );
        sysRole.setRoleKey( dto.getRoleKey() );
        sysRole.setRoleSort( dto.getRoleSort() );
        sysRole.setDataScope( dto.getDataScope() );
        sysRole.setMenuCheckStrictly( dto.getMenuCheckStrictly() );
        sysRole.setDeptCheckStrictly( dto.getDeptCheckStrictly() );
        sysRole.setStatus( dto.getStatus() );
        sysRole.setRemark( dto.getRemark() );

        return sysRole;
    }

    @Override
    public SysRole updateDtoToEntity(RoleUpdateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysRole sysRole = new SysRole();

        sysRole.setRoleId( dto.getRoleId() );
        sysRole.setRoleName( dto.getRoleName() );
        sysRole.setRoleKey( dto.getRoleKey() );
        sysRole.setRoleSort( dto.getRoleSort() );
        sysRole.setDataScope( dto.getDataScope() );
        sysRole.setMenuCheckStrictly( dto.getMenuCheckStrictly() );
        sysRole.setDeptCheckStrictly( dto.getDeptCheckStrictly() );
        sysRole.setStatus( dto.getStatus() );
        sysRole.setRemark( dto.getRemark() );

        return sysRole;
    }

    @Override
    public void updateEntityFromDto(RoleUpdateDTO dto, SysRole entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getRoleId() != null ) {
            entity.setRoleId( dto.getRoleId() );
        }
        if ( dto.getRoleName() != null ) {
            entity.setRoleName( dto.getRoleName() );
        }
        if ( dto.getRoleKey() != null ) {
            entity.setRoleKey( dto.getRoleKey() );
        }
        if ( dto.getRoleSort() != null ) {
            entity.setRoleSort( dto.getRoleSort() );
        }
        if ( dto.getDataScope() != null ) {
            entity.setDataScope( dto.getDataScope() );
        }
        if ( dto.getMenuCheckStrictly() != null ) {
            entity.setMenuCheckStrictly( dto.getMenuCheckStrictly() );
        }
        if ( dto.getDeptCheckStrictly() != null ) {
            entity.setDeptCheckStrictly( dto.getDeptCheckStrictly() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

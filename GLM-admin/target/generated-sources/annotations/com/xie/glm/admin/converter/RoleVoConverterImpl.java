package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.RoleVO;
import com.xie.glm.system.dto.RoleDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T03:03:26+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class RoleVoConverterImpl implements RoleVoConverter {

    @Override
    public RoleVO toVo(RoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RoleVO.RoleVOBuilder<?, ?> roleVO = RoleVO.builder();

        roleVO.roleId( dto.getRoleId() );
        roleVO.roleName( dto.getRoleName() );
        roleVO.roleKey( dto.getRoleKey() );
        roleVO.roleSort( dto.getRoleSort() );
        roleVO.dataScope( dto.getDataScope() );
        roleVO.menuCheckStrictly( dto.getMenuCheckStrictly() );
        roleVO.deptCheckStrictly( dto.getDeptCheckStrictly() );
        roleVO.status( dto.getStatus() );
        roleVO.remark( dto.getRemark() );
        roleVO.createTime( dto.getCreateTime() );
        roleVO.updateTime( dto.getUpdateTime() );
        List<Long> list = dto.getMenuIds();
        if ( list != null ) {
            roleVO.menuIds( new ArrayList<Long>( list ) );
        }

        return roleVO.build();
    }

    @Override
    public List<RoleVO> toVoList(List<RoleDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<RoleVO> list = new ArrayList<RoleVO>( dtos.size() );
        for ( RoleDTO roleDTO : dtos ) {
            list.add( toVo( roleDTO ) );
        }

        return list;
    }
}

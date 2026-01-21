package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDept;
import com.xie.glm.system.dto.DeptDTO;
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
public class DeptConverterImpl implements DeptConverter {

    @Override
    public DeptDTO toDto(SysDept entity) {
        if ( entity == null ) {
            return null;
        }

        DeptDTO.DeptDTOBuilder<?, ?> deptDTO = DeptDTO.builder();

        deptDTO.deptId( entity.getDeptId() );
        deptDTO.deptName( entity.getDeptName() );
        deptDTO.parentId( entity.getParentId() );
        deptDTO.orderNum( entity.getOrderNum() );
        deptDTO.leader( entity.getLeader() );
        deptDTO.phone( entity.getPhone() );
        deptDTO.email( entity.getEmail() );
        deptDTO.status( entity.getStatus() );
        deptDTO.ancestors( entity.getAncestors() );
        deptDTO.createTime( entity.getCreateTime() );
        deptDTO.updateTime( entity.getUpdateTime() );

        return deptDTO.build();
    }

    @Override
    public SysDept toEntity(DeptDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysDept sysDept = new SysDept();

        sysDept.setCreateTime( dto.getCreateTime() );
        sysDept.setUpdateTime( dto.getUpdateTime() );
        sysDept.setDeptId( dto.getDeptId() );
        sysDept.setDeptName( dto.getDeptName() );
        sysDept.setParentId( dto.getParentId() );
        sysDept.setOrderNum( dto.getOrderNum() );
        sysDept.setLeader( dto.getLeader() );
        sysDept.setPhone( dto.getPhone() );
        sysDept.setEmail( dto.getEmail() );
        sysDept.setStatus( dto.getStatus() );
        sysDept.setAncestors( dto.getAncestors() );

        return sysDept;
    }

    @Override
    public List<DeptDTO> toDtoList(List<SysDept> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DeptDTO> list = new ArrayList<DeptDTO>( entities.size() );
        for ( SysDept sysDept : entities ) {
            list.add( toDto( sysDept ) );
        }

        return list;
    }
}

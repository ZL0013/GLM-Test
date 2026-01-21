package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDictType;
import com.xie.glm.system.dto.DictTypeDTO;
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
public class DictTypeConverterImpl implements DictTypeConverter {

    @Override
    public DictTypeDTO toDto(SysDictType entity) {
        if ( entity == null ) {
            return null;
        }

        DictTypeDTO.DictTypeDTOBuilder<?, ?> dictTypeDTO = DictTypeDTO.builder();

        dictTypeDTO.dictId( entity.getDictId() );
        dictTypeDTO.dictName( entity.getDictName() );
        dictTypeDTO.dictType( entity.getDictType() );
        dictTypeDTO.status( entity.getStatus() );
        dictTypeDTO.remark( entity.getRemark() );
        dictTypeDTO.createTime( entity.getCreateTime() );
        dictTypeDTO.updateTime( entity.getUpdateTime() );

        return dictTypeDTO.build();
    }

    @Override
    public List<DictTypeDTO> toDtoList(List<SysDictType> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DictTypeDTO> list = new ArrayList<DictTypeDTO>( entities.size() );
        for ( SysDictType sysDictType : entities ) {
            list.add( toDto( sysDictType ) );
        }

        return list;
    }

    @Override
    public SysDictType dtoToEntity(DictTypeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysDictType sysDictType = new SysDictType();

        sysDictType.setCreateTime( dto.getCreateTime() );
        sysDictType.setUpdateTime( dto.getUpdateTime() );
        sysDictType.setDictId( dto.getDictId() );
        sysDictType.setDictName( dto.getDictName() );
        sysDictType.setDictType( dto.getDictType() );
        sysDictType.setStatus( dto.getStatus() );
        sysDictType.setRemark( dto.getRemark() );

        return sysDictType;
    }

    @Override
    public void updateEntityFromDto(DictTypeDTO dto, SysDictType entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreateTime() != null ) {
            entity.setCreateTime( dto.getCreateTime() );
        }
        if ( dto.getUpdateTime() != null ) {
            entity.setUpdateTime( dto.getUpdateTime() );
        }
        if ( dto.getDictId() != null ) {
            entity.setDictId( dto.getDictId() );
        }
        if ( dto.getDictName() != null ) {
            entity.setDictName( dto.getDictName() );
        }
        if ( dto.getDictType() != null ) {
            entity.setDictType( dto.getDictType() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

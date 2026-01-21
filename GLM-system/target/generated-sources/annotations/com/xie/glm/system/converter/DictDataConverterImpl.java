package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysDictData;
import com.xie.glm.system.dto.DictDataDTO;
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
public class DictDataConverterImpl implements DictDataConverter {

    @Override
    public DictDataDTO toDto(SysDictData entity) {
        if ( entity == null ) {
            return null;
        }

        DictDataDTO.DictDataDTOBuilder<?, ?> dictDataDTO = DictDataDTO.builder();

        dictDataDTO.dictCode( entity.getDictCode() );
        dictDataDTO.dictSort( entity.getDictSort() );
        dictDataDTO.dictLabel( entity.getDictLabel() );
        dictDataDTO.dictValue( entity.getDictValue() );
        dictDataDTO.dictType( entity.getDictType() );
        dictDataDTO.cssClass( entity.getCssClass() );
        dictDataDTO.listClass( entity.getListClass() );
        dictDataDTO.isDefault( entity.getIsDefault() );
        dictDataDTO.status( entity.getStatus() );
        dictDataDTO.remark( entity.getRemark() );
        dictDataDTO.createTime( entity.getCreateTime() );
        dictDataDTO.updateTime( entity.getUpdateTime() );

        return dictDataDTO.build();
    }

    @Override
    public List<DictDataDTO> toDtoList(List<SysDictData> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DictDataDTO> list = new ArrayList<DictDataDTO>( entities.size() );
        for ( SysDictData sysDictData : entities ) {
            list.add( toDto( sysDictData ) );
        }

        return list;
    }

    @Override
    public SysDictData dtoToEntity(DictDataDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysDictData sysDictData = new SysDictData();

        sysDictData.setCreateTime( dto.getCreateTime() );
        sysDictData.setUpdateTime( dto.getUpdateTime() );
        sysDictData.setDictCode( dto.getDictCode() );
        sysDictData.setDictSort( dto.getDictSort() );
        sysDictData.setDictLabel( dto.getDictLabel() );
        sysDictData.setDictValue( dto.getDictValue() );
        sysDictData.setDictType( dto.getDictType() );
        sysDictData.setCssClass( dto.getCssClass() );
        sysDictData.setListClass( dto.getListClass() );
        sysDictData.setIsDefault( dto.getIsDefault() );
        sysDictData.setStatus( dto.getStatus() );
        sysDictData.setRemark( dto.getRemark() );

        return sysDictData;
    }

    @Override
    public void updateEntityFromDto(DictDataDTO dto, SysDictData entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreateTime() != null ) {
            entity.setCreateTime( dto.getCreateTime() );
        }
        if ( dto.getUpdateTime() != null ) {
            entity.setUpdateTime( dto.getUpdateTime() );
        }
        if ( dto.getDictCode() != null ) {
            entity.setDictCode( dto.getDictCode() );
        }
        if ( dto.getDictSort() != null ) {
            entity.setDictSort( dto.getDictSort() );
        }
        if ( dto.getDictLabel() != null ) {
            entity.setDictLabel( dto.getDictLabel() );
        }
        if ( dto.getDictValue() != null ) {
            entity.setDictValue( dto.getDictValue() );
        }
        if ( dto.getDictType() != null ) {
            entity.setDictType( dto.getDictType() );
        }
        if ( dto.getCssClass() != null ) {
            entity.setCssClass( dto.getCssClass() );
        }
        if ( dto.getListClass() != null ) {
            entity.setListClass( dto.getListClass() );
        }
        if ( dto.getIsDefault() != null ) {
            entity.setIsDefault( dto.getIsDefault() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

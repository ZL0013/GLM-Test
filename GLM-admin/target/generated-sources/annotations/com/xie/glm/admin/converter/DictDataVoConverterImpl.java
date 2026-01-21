package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DictDataVO;
import com.xie.glm.system.dto.DictDataDTO;
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
public class DictDataVoConverterImpl implements DictDataVoConverter {

    @Override
    public DictDataVO toVo(DictDataDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DictDataVO.DictDataVOBuilder<?, ?> dictDataVO = DictDataVO.builder();

        dictDataVO.dictCode( dto.getDictCode() );
        dictDataVO.dictSort( dto.getDictSort() );
        dictDataVO.dictLabel( dto.getDictLabel() );
        dictDataVO.dictValue( dto.getDictValue() );
        dictDataVO.dictType( dto.getDictType() );
        dictDataVO.cssClass( dto.getCssClass() );
        dictDataVO.listClass( dto.getListClass() );
        dictDataVO.isDefault( dto.getIsDefault() );
        dictDataVO.status( dto.getStatus() );
        dictDataVO.remark( dto.getRemark() );
        dictDataVO.createTime( dto.getCreateTime() );
        dictDataVO.updateTime( dto.getUpdateTime() );

        return dictDataVO.build();
    }

    @Override
    public List<DictDataVO> toVoList(List<DictDataDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<DictDataVO> list = new ArrayList<DictDataVO>( dtos.size() );
        for ( DictDataDTO dictDataDTO : dtos ) {
            list.add( toVo( dictDataDTO ) );
        }

        return list;
    }
}

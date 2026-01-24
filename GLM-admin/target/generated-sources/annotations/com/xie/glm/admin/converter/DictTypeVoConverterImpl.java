package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DictTypeVO;
import com.xie.glm.system.dto.DictTypeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T14:15:06+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class DictTypeVoConverterImpl implements DictTypeVoConverter {

    @Override
    public DictTypeVO toVo(DictTypeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DictTypeVO.DictTypeVOBuilder<?, ?> dictTypeVO = DictTypeVO.builder();

        dictTypeVO.dictId( dto.getDictId() );
        dictTypeVO.dictName( dto.getDictName() );
        dictTypeVO.dictType( dto.getDictType() );
        dictTypeVO.status( dto.getStatus() );
        dictTypeVO.remark( dto.getRemark() );
        dictTypeVO.createTime( dto.getCreateTime() );
        dictTypeVO.updateTime( dto.getUpdateTime() );

        return dictTypeVO.build();
    }

    @Override
    public List<DictTypeVO> toVoList(List<DictTypeDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<DictTypeVO> list = new ArrayList<DictTypeVO>( dtos.size() );
        for ( DictTypeDTO dictTypeDTO : dtos ) {
            list.add( toVo( dictTypeDTO ) );
        }

        return list;
    }
}

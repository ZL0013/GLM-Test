package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.OperLogVO;
import com.xie.glm.system.dto.OperLogDTO;
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
public class OperLogVoConverterImpl implements OperLogVoConverter {

    @Override
    public OperLogVO toVo(OperLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OperLogVO.OperLogVOBuilder<?, ?> operLogVO = OperLogVO.builder();

        operLogVO.operId( dto.getOperId() );
        operLogVO.title( dto.getTitle() );
        operLogVO.businessType( dto.getBusinessType() );
        operLogVO.businessTypeName( dto.getBusinessTypeName() );
        operLogVO.method( dto.getMethod() );
        operLogVO.requestMethod( dto.getRequestMethod() );
        operLogVO.operatorType( dto.getOperatorType() );
        operLogVO.operatorTypeName( dto.getOperatorTypeName() );
        operLogVO.operName( dto.getOperName() );
        operLogVO.deptName( dto.getDeptName() );
        operLogVO.operUrl( dto.getOperUrl() );
        operLogVO.operIp( dto.getOperIp() );
        operLogVO.operLocation( dto.getOperLocation() );
        operLogVO.operParam( dto.getOperParam() );
        operLogVO.jsonResult( dto.getJsonResult() );
        operLogVO.status( dto.getStatus() );
        operLogVO.statusName( dto.getStatusName() );
        operLogVO.errorMsg( dto.getErrorMsg() );
        operLogVO.operTime( dto.getOperTime() );
        operLogVO.costTime( dto.getCostTime() );

        return operLogVO.build();
    }

    @Override
    public List<OperLogVO> toVoList(List<OperLogDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<OperLogVO> list = new ArrayList<OperLogVO>( dtos.size() );
        for ( OperLogDTO operLogDTO : dtos ) {
            list.add( toVo( operLogDTO ) );
        }

        return list;
    }
}

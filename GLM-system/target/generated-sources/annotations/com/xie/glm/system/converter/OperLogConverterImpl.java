package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysOperLog;
import com.xie.glm.system.dto.OperLogDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T14:15:03+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OperLogConverterImpl implements OperLogConverter {

    @Override
    public OperLogDTO toDto(SysOperLog entity) {
        if ( entity == null ) {
            return null;
        }

        OperLogDTO.OperLogDTOBuilder<?, ?> operLogDTO = OperLogDTO.builder();

        operLogDTO.businessTypeName( businessTypeToName( entity.getBusinessType() ) );
        operLogDTO.operatorTypeName( operatorTypeToName( entity.getOperatorType() ) );
        operLogDTO.statusName( statusToName( entity.getStatus() ) );
        operLogDTO.operId( entity.getOperId() );
        operLogDTO.title( entity.getTitle() );
        operLogDTO.businessType( entity.getBusinessType() );
        operLogDTO.method( entity.getMethod() );
        operLogDTO.requestMethod( entity.getRequestMethod() );
        operLogDTO.operatorType( entity.getOperatorType() );
        operLogDTO.operName( entity.getOperName() );
        operLogDTO.deptName( entity.getDeptName() );
        operLogDTO.operUrl( entity.getOperUrl() );
        operLogDTO.operIp( entity.getOperIp() );
        operLogDTO.operLocation( entity.getOperLocation() );
        operLogDTO.operParam( entity.getOperParam() );
        operLogDTO.jsonResult( entity.getJsonResult() );
        operLogDTO.status( entity.getStatus() );
        operLogDTO.errorMsg( entity.getErrorMsg() );
        operLogDTO.operTime( entity.getOperTime() );
        operLogDTO.costTime( entity.getCostTime() );

        return operLogDTO.build();
    }

    @Override
    public List<OperLogDTO> toDtoList(List<SysOperLog> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OperLogDTO> list = new ArrayList<OperLogDTO>( entities.size() );
        for ( SysOperLog sysOperLog : entities ) {
            list.add( toDto( sysOperLog ) );
        }

        return list;
    }

    @Override
    public SysOperLog toEntity(OperLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysOperLog.SysOperLogBuilder sysOperLog = SysOperLog.builder();

        sysOperLog.operId( dto.getOperId() );
        sysOperLog.title( dto.getTitle() );
        sysOperLog.businessType( dto.getBusinessType() );
        sysOperLog.method( dto.getMethod() );
        sysOperLog.requestMethod( dto.getRequestMethod() );
        sysOperLog.operatorType( dto.getOperatorType() );
        sysOperLog.operName( dto.getOperName() );
        sysOperLog.deptName( dto.getDeptName() );
        sysOperLog.operUrl( dto.getOperUrl() );
        sysOperLog.operIp( dto.getOperIp() );
        sysOperLog.operLocation( dto.getOperLocation() );
        sysOperLog.operParam( dto.getOperParam() );
        sysOperLog.jsonResult( dto.getJsonResult() );
        sysOperLog.status( dto.getStatus() );
        sysOperLog.errorMsg( dto.getErrorMsg() );
        sysOperLog.operTime( dto.getOperTime() );
        sysOperLog.costTime( dto.getCostTime() );

        return sysOperLog.build();
    }
}

package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysConfig;
import com.xie.glm.system.dto.ConfigDTO;
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
public class ConfigConverterImpl implements ConfigConverter {

    @Override
    public ConfigDTO toDto(SysConfig entity) {
        if ( entity == null ) {
            return null;
        }

        ConfigDTO.ConfigDTOBuilder<?, ?> configDTO = ConfigDTO.builder();

        configDTO.configId( entity.getConfigId() );
        configDTO.configName( entity.getConfigName() );
        configDTO.configKey( entity.getConfigKey() );
        configDTO.configValue( entity.getConfigValue() );
        configDTO.configType( entity.getConfigType() );
        configDTO.remark( entity.getRemark() );
        configDTO.createTime( entity.getCreateTime() );
        configDTO.updateTime( entity.getUpdateTime() );
        configDTO.createdBy( entity.getCreatedBy() );
        configDTO.updatedBy( entity.getUpdatedBy() );

        return configDTO.build();
    }

    @Override
    public SysConfig toEntity(ConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysConfig sysConfig = new SysConfig();

        sysConfig.setCreateTime( dto.getCreateTime() );
        sysConfig.setUpdateTime( dto.getUpdateTime() );
        sysConfig.setCreatedBy( dto.getCreatedBy() );
        sysConfig.setUpdatedBy( dto.getUpdatedBy() );
        sysConfig.setConfigId( dto.getConfigId() );
        sysConfig.setConfigName( dto.getConfigName() );
        sysConfig.setConfigKey( dto.getConfigKey() );
        sysConfig.setConfigValue( dto.getConfigValue() );
        sysConfig.setConfigType( dto.getConfigType() );
        sysConfig.setRemark( dto.getRemark() );

        return sysConfig;
    }

    @Override
    public List<ConfigDTO> toDtoList(List<SysConfig> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ConfigDTO> list = new ArrayList<ConfigDTO>( entities.size() );
        for ( SysConfig sysConfig : entities ) {
            list.add( toDto( sysConfig ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(ConfigDTO dto, SysConfig entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreateTime() != null ) {
            entity.setCreateTime( dto.getCreateTime() );
        }
        if ( dto.getUpdateTime() != null ) {
            entity.setUpdateTime( dto.getUpdateTime() );
        }
        if ( dto.getCreatedBy() != null ) {
            entity.setCreatedBy( dto.getCreatedBy() );
        }
        if ( dto.getUpdatedBy() != null ) {
            entity.setUpdatedBy( dto.getUpdatedBy() );
        }
        if ( dto.getConfigId() != null ) {
            entity.setConfigId( dto.getConfigId() );
        }
        if ( dto.getConfigName() != null ) {
            entity.setConfigName( dto.getConfigName() );
        }
        if ( dto.getConfigKey() != null ) {
            entity.setConfigKey( dto.getConfigKey() );
        }
        if ( dto.getConfigValue() != null ) {
            entity.setConfigValue( dto.getConfigValue() );
        }
        if ( dto.getConfigType() != null ) {
            entity.setConfigType( dto.getConfigType() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

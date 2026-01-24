package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.ConfigVO;
import com.xie.glm.system.dto.ConfigDTO;
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
public class ConfigVoConverterImpl implements ConfigVoConverter {

    @Override
    public ConfigVO toVo(ConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ConfigVO.ConfigVOBuilder<?, ?> configVO = ConfigVO.builder();

        configVO.configId( dto.getConfigId() );
        configVO.configName( dto.getConfigName() );
        configVO.configKey( dto.getConfigKey() );
        configVO.configValue( dto.getConfigValue() );
        configVO.configType( dto.getConfigType() );
        configVO.remark( dto.getRemark() );
        configVO.createTime( dto.getCreateTime() );
        configVO.updateTime( dto.getUpdateTime() );
        configVO.createdBy( dto.getCreatedBy() );
        configVO.updatedBy( dto.getUpdatedBy() );

        return configVO.build();
    }

    @Override
    public List<ConfigVO> toVoList(List<ConfigDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ConfigVO> list = new ArrayList<ConfigVO>( dtos.size() );
        for ( ConfigDTO configDTO : dtos ) {
            list.add( toVo( configDTO ) );
        }

        return list;
    }
}

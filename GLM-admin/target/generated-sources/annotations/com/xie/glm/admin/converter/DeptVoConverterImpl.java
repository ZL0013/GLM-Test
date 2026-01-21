package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.DeptVO;
import com.xie.glm.system.dto.DeptDTO;
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
public class DeptVoConverterImpl implements DeptVoConverter {

    @Override
    public DeptVO toVo(DeptDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DeptVO.DeptVOBuilder<?, ?> deptVO = DeptVO.builder();

        deptVO.deptId( dto.getDeptId() );
        deptVO.deptName( dto.getDeptName() );
        deptVO.parentId( dto.getParentId() );
        deptVO.orderNum( dto.getOrderNum() );
        deptVO.leader( dto.getLeader() );
        deptVO.phone( dto.getPhone() );
        deptVO.email( dto.getEmail() );
        deptVO.status( dto.getStatus() );
        deptVO.ancestors( dto.getAncestors() );
        deptVO.createTime( dto.getCreateTime() );
        deptVO.updateTime( dto.getUpdateTime() );

        return deptVO.build();
    }

    @Override
    public List<DeptVO> toVoList(List<DeptDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DeptVO> list = new ArrayList<DeptVO>( dtoList.size() );
        for ( DeptDTO deptDTO : dtoList ) {
            list.add( toVo( deptDTO ) );
        }

        return list;
    }
}

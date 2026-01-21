package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.NoticeVO;
import com.xie.glm.system.dto.NoticeDTO;
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
public class NoticeVoConverterImpl implements NoticeVoConverter {

    @Override
    public NoticeVO toVo(NoticeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NoticeVO noticeVO = new NoticeVO();

        noticeVO.setNoticeTypeName( typeToName( dto.getNoticeType() ) );
        noticeVO.setStatusName( statusToName( dto.getStatus() ) );
        noticeVO.setNoticeId( dto.getNoticeId() );
        noticeVO.setNoticeTitle( dto.getNoticeTitle() );
        noticeVO.setNoticeContent( dto.getNoticeContent() );
        noticeVO.setCreateBy( dto.getCreateBy() );
        noticeVO.setCreateTime( dto.getCreateTime() );
        noticeVO.setUpdateBy( dto.getUpdateBy() );
        noticeVO.setUpdateTime( dto.getUpdateTime() );
        noticeVO.setRemark( dto.getRemark() );

        return noticeVO;
    }

    @Override
    public List<NoticeVO> toVoList(List<NoticeDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<NoticeVO> list = new ArrayList<NoticeVO>( dtos.size() );
        for ( NoticeDTO noticeDTO : dtos ) {
            list.add( toVo( noticeDTO ) );
        }

        return list;
    }
}

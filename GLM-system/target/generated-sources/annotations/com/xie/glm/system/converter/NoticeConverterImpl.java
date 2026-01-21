package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysNotice;
import com.xie.glm.system.dto.NoticeDTO;
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
public class NoticeConverterImpl implements NoticeConverter {

    @Override
    public NoticeDTO toDto(SysNotice entity) {
        if ( entity == null ) {
            return null;
        }

        NoticeDTO noticeDTO = new NoticeDTO();

        noticeDTO.setCreateBy( entity.getCreatedBy() );
        noticeDTO.setUpdateBy( entity.getUpdatedBy() );
        noticeDTO.setNoticeId( entity.getNoticeId() );
        noticeDTO.setNoticeTitle( entity.getNoticeTitle() );
        noticeDTO.setNoticeType( entity.getNoticeType() );
        noticeDTO.setNoticeContent( entity.getNoticeContent() );
        noticeDTO.setStatus( entity.getStatus() );
        noticeDTO.setCreateTime( entity.getCreateTime() );
        noticeDTO.setUpdateTime( entity.getUpdateTime() );
        noticeDTO.setRemark( entity.getRemark() );

        return noticeDTO;
    }

    @Override
    public SysNotice toEntity(NoticeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysNotice sysNotice = new SysNotice();

        sysNotice.setCreatedBy( dto.getCreateBy() );
        sysNotice.setUpdatedBy( dto.getUpdateBy() );
        sysNotice.setCreateTime( dto.getCreateTime() );
        sysNotice.setUpdateTime( dto.getUpdateTime() );
        sysNotice.setNoticeId( dto.getNoticeId() );
        sysNotice.setNoticeTitle( dto.getNoticeTitle() );
        sysNotice.setNoticeType( dto.getNoticeType() );
        sysNotice.setNoticeContent( dto.getNoticeContent() );
        sysNotice.setStatus( dto.getStatus() );
        sysNotice.setRemark( dto.getRemark() );

        return sysNotice;
    }

    @Override
    public List<NoticeDTO> toDtoList(List<SysNotice> entities) {
        if ( entities == null ) {
            return null;
        }

        List<NoticeDTO> list = new ArrayList<NoticeDTO>( entities.size() );
        for ( SysNotice sysNotice : entities ) {
            list.add( toDto( sysNotice ) );
        }

        return list;
    }
}

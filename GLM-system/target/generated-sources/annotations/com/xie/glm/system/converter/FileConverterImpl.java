package com.xie.glm.system.converter;

import com.xie.glm.common.dto.FileDTO;
import com.xie.glm.system.domain.SysFile;
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
public class FileConverterImpl implements FileConverter {

    @Override
    public FileDTO toDto(SysFile entity) {
        if ( entity == null ) {
            return null;
        }

        FileDTO.Builder fileDTO = FileDTO.builder();

        fileDTO.id( entity.getFileId() );
        fileDTO.fileName( entity.getFileName() );
        fileDTO.filePath( entity.getFilePath() );
        fileDTO.contentType( entity.getContentType() );
        fileDTO.fileSize( entity.getFileSize() );
        fileDTO.fileMd5( entity.getFileMd5() );
        fileDTO.createTime( entity.getCreateTime() );
        fileDTO.updateTime( entity.getUpdateTime() );

        return fileDTO.build();
    }

    @Override
    public SysFile toEntity(FileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysFile sysFile = new SysFile();

        sysFile.setFileId( dto.id() );
        sysFile.setCreateTime( dto.createTime() );
        sysFile.setUpdateTime( dto.updateTime() );
        sysFile.setFileName( dto.fileName() );
        sysFile.setFilePath( dto.filePath() );
        sysFile.setContentType( dto.contentType() );
        sysFile.setFileSize( dto.fileSize() );
        sysFile.setFileMd5( dto.fileMd5() );

        return sysFile;
    }

    @Override
    public List<FileDTO> toDtoList(List<SysFile> entities) {
        if ( entities == null ) {
            return null;
        }

        List<FileDTO> list = new ArrayList<FileDTO>( entities.size() );
        for ( SysFile sysFile : entities ) {
            list.add( toDto( sysFile ) );
        }

        return list;
    }
}

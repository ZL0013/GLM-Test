package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.FileVO;
import com.xie.glm.common.dto.FileDTO;
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
public class FileVoConverterImpl implements FileVoConverter {

    @Override
    public FileVO toVo(FileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        FileVO.Builder fileVO = FileVO.builder();

        fileVO.id( dto.id() );
        fileVO.fileName( dto.fileName() );
        fileVO.filePath( dto.filePath() );
        fileVO.contentType( dto.contentType() );
        fileVO.fileSize( dto.fileSize() );
        fileVO.fileMd5( dto.fileMd5() );
        fileVO.createTime( dto.createTime() );
        fileVO.updateTime( dto.updateTime() );

        fileVO.fileSizeFormatted( formatFileSize(dto.fileSize()) );

        return fileVO.build();
    }

    @Override
    public List<FileVO> toVoList(List<FileDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<FileVO> list = new ArrayList<FileVO>( dtos.size() );
        for ( FileDTO fileDTO : dtos ) {
            list.add( toVo( fileDTO ) );
        }

        return list;
    }
}

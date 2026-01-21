package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.JobVO;
import com.xie.glm.system.dto.JobDTO;
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
public class JobVoConverterImpl implements JobVoConverter {

    @Override
    public JobVO toVo(JobDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobVO.JobVOBuilder<?, ?> jobVO = JobVO.builder();

        jobVO.jobId( dto.getJobId() );
        jobVO.jobName( dto.getJobName() );
        jobVO.jobGroup( dto.getJobGroup() );
        jobVO.invokeTarget( dto.getInvokeTarget() );
        jobVO.invokeParam( dto.getInvokeParam() );
        jobVO.cronExpression( dto.getCronExpression() );
        jobVO.misfirePolicy( dto.getMisfirePolicy() );
        jobVO.concurrent( dto.getConcurrent() );
        jobVO.status( dto.getStatus() );
        jobVO.beginTime( dto.getBeginTime() );
        jobVO.nextValidTime( dto.getNextValidTime() );
        jobVO.createTime( dto.getCreateTime() );
        jobVO.updateTime( dto.getUpdateTime() );
        jobVO.exceptionInfo( dto.getExceptionInfo() );
        jobVO.remark( dto.getRemark() );

        return jobVO.build();
    }

    @Override
    public List<JobVO> toVoList(List<JobDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<JobVO> list = new ArrayList<JobVO>( dtos.size() );
        for ( JobDTO jobDTO : dtos ) {
            list.add( toVo( jobDTO ) );
        }

        return list;
    }
}

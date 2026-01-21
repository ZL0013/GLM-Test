package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysJob;
import com.xie.glm.system.dto.JobDTO;
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
public class JobConverterImpl implements JobConverter {

    @Override
    public JobDTO toDto(SysJob entity) {
        if ( entity == null ) {
            return null;
        }

        JobDTO.JobDTOBuilder<?, ?> jobDTO = JobDTO.builder();

        jobDTO.jobId( entity.getJobId() );
        jobDTO.jobName( entity.getJobName() );
        jobDTO.jobGroup( entity.getJobGroup() );
        jobDTO.invokeTarget( entity.getInvokeTarget() );
        jobDTO.invokeParam( entity.getInvokeParam() );
        jobDTO.cronExpression( entity.getCronExpression() );
        jobDTO.misfirePolicy( entity.getMisfirePolicy() );
        jobDTO.concurrent( entity.getConcurrent() );
        jobDTO.status( entity.getStatus() );
        jobDTO.beginTime( entity.getBeginTime() );
        jobDTO.nextValidTime( entity.getNextValidTime() );
        jobDTO.createTime( entity.getCreateTime() );
        jobDTO.updateTime( entity.getUpdateTime() );
        jobDTO.exceptionInfo( entity.getExceptionInfo() );
        jobDTO.remark( entity.getRemark() );

        return jobDTO.build();
    }

    @Override
    public SysJob toEntity(JobDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysJob sysJob = new SysJob();

        sysJob.setCreateTime( dto.getCreateTime() );
        sysJob.setUpdateTime( dto.getUpdateTime() );
        sysJob.setJobId( dto.getJobId() );
        sysJob.setJobName( dto.getJobName() );
        sysJob.setJobGroup( dto.getJobGroup() );
        sysJob.setInvokeTarget( dto.getInvokeTarget() );
        sysJob.setInvokeParam( dto.getInvokeParam() );
        sysJob.setCronExpression( dto.getCronExpression() );
        sysJob.setMisfirePolicy( dto.getMisfirePolicy() );
        sysJob.setConcurrent( dto.getConcurrent() );
        sysJob.setStatus( dto.getStatus() );
        sysJob.setBeginTime( dto.getBeginTime() );
        sysJob.setNextValidTime( dto.getNextValidTime() );
        sysJob.setExceptionInfo( dto.getExceptionInfo() );
        sysJob.setRemark( dto.getRemark() );

        return sysJob;
    }

    @Override
    public List<JobDTO> toDtoList(List<SysJob> entities) {
        if ( entities == null ) {
            return null;
        }

        List<JobDTO> list = new ArrayList<JobDTO>( entities.size() );
        for ( SysJob sysJob : entities ) {
            list.add( toDto( sysJob ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(JobDTO dto, SysJob entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreateTime() != null ) {
            entity.setCreateTime( dto.getCreateTime() );
        }
        if ( dto.getUpdateTime() != null ) {
            entity.setUpdateTime( dto.getUpdateTime() );
        }
        if ( dto.getJobId() != null ) {
            entity.setJobId( dto.getJobId() );
        }
        if ( dto.getJobName() != null ) {
            entity.setJobName( dto.getJobName() );
        }
        if ( dto.getJobGroup() != null ) {
            entity.setJobGroup( dto.getJobGroup() );
        }
        if ( dto.getInvokeTarget() != null ) {
            entity.setInvokeTarget( dto.getInvokeTarget() );
        }
        if ( dto.getInvokeParam() != null ) {
            entity.setInvokeParam( dto.getInvokeParam() );
        }
        if ( dto.getCronExpression() != null ) {
            entity.setCronExpression( dto.getCronExpression() );
        }
        if ( dto.getMisfirePolicy() != null ) {
            entity.setMisfirePolicy( dto.getMisfirePolicy() );
        }
        if ( dto.getConcurrent() != null ) {
            entity.setConcurrent( dto.getConcurrent() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getBeginTime() != null ) {
            entity.setBeginTime( dto.getBeginTime() );
        }
        if ( dto.getNextValidTime() != null ) {
            entity.setNextValidTime( dto.getNextValidTime() );
        }
        if ( dto.getExceptionInfo() != null ) {
            entity.setExceptionInfo( dto.getExceptionInfo() );
        }
        if ( dto.getRemark() != null ) {
            entity.setRemark( dto.getRemark() );
        }
    }
}

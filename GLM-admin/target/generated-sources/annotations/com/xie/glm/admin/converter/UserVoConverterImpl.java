package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.UserVO;
import com.xie.glm.system.dto.UserDTO;
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
public class UserVoConverterImpl implements UserVoConverter {

    @Override
    public UserVO toVo(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserVO.UserVOBuilder<?, ?> userVO = UserVO.builder();

        userVO.userId( dto.getUserId() );
        userVO.userName( dto.getUserName() );
        userVO.nickName( dto.getNickName() );
        userVO.password( dto.getPassword() );
        userVO.email( dto.getEmail() );
        userVO.phonenumber( dto.getPhonenumber() );
        userVO.sex( dto.getSex() );
        userVO.avatar( dto.getAvatar() );
        userVO.deptId( dto.getDeptId() );
        userVO.status( dto.getStatus() );
        userVO.remark( dto.getRemark() );
        userVO.createTime( dto.getCreateTime() );
        userVO.updateTime( dto.getUpdateTime() );
        userVO.loginDate( dto.getLoginDate() );
        userVO.pwdUpdateDate( dto.getPwdUpdateDate() );
        userVO.passwordChanged( dto.getPasswordChanged() );
        userVO.defaultPassword( dto.getDefaultPassword() );

        return userVO.build();
    }

    @Override
    public List<UserVO> toVoList(List<UserDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<UserVO> list = new ArrayList<UserVO>( dtos.size() );
        for ( UserDTO userDTO : dtos ) {
            list.add( toVo( userDTO ) );
        }

        return list;
    }
}

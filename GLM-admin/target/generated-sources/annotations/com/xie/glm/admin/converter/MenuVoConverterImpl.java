package com.xie.glm.admin.converter;

import com.xie.glm.admin.vo.MenuVO;
import com.xie.glm.system.dto.MenuDTO;
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
public class MenuVoConverterImpl implements MenuVoConverter {

    @Override
    public MenuVO toVo(MenuDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MenuVO.MenuVOBuilder<?, ?> menuVO = MenuVO.builder();

        menuVO.menuId( dto.getMenuId() );
        menuVO.menuName( dto.getMenuName() );
        menuVO.parentId( dto.getParentId() );
        menuVO.orderNum( dto.getOrderNum() );
        menuVO.path( dto.getPath() );
        menuVO.component( dto.getComponent() );
        menuVO.query( dto.getQuery() );
        menuVO.routeName( dto.getRouteName() );
        menuVO.isFrame( dto.getIsFrame() );
        menuVO.isCache( dto.getIsCache() );
        menuVO.menuType( dto.getMenuType() );
        menuVO.visible( dto.getVisible() );
        menuVO.status( dto.getStatus() );
        menuVO.perms( dto.getPerms() );
        menuVO.icon( dto.getIcon() );
        menuVO.createTime( dto.getCreateTime() );
        menuVO.updateTime( dto.getUpdateTime() );

        return menuVO.build();
    }

    @Override
    public List<MenuVO> toVoList(List<MenuDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<MenuVO> list = new ArrayList<MenuVO>( dtos.size() );
        for ( MenuDTO menuDTO : dtos ) {
            list.add( toVo( menuDTO ) );
        }

        return list;
    }
}

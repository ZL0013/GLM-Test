package com.xie.glm.system.converter;

import com.xie.glm.system.domain.SysMenu;
import com.xie.glm.system.dto.MenuDTO;
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
public class MenuConverterImpl implements MenuConverter {

    @Override
    public MenuDTO toDto(SysMenu entity) {
        if ( entity == null ) {
            return null;
        }

        MenuDTO.MenuDTOBuilder<?, ?> menuDTO = MenuDTO.builder();

        menuDTO.menuId( entity.getMenuId() );
        menuDTO.menuName( entity.getMenuName() );
        menuDTO.parentId( entity.getParentId() );
        menuDTO.orderNum( entity.getOrderNum() );
        menuDTO.path( entity.getPath() );
        menuDTO.component( entity.getComponent() );
        menuDTO.query( entity.getQuery() );
        menuDTO.routeName( entity.getRouteName() );
        menuDTO.isFrame( entity.getIsFrame() );
        menuDTO.isCache( entity.getIsCache() );
        menuDTO.menuType( entity.getMenuType() );
        menuDTO.visible( entity.getVisible() );
        menuDTO.status( entity.getStatus() );
        menuDTO.perms( entity.getPerms() );
        menuDTO.icon( entity.getIcon() );
        menuDTO.createTime( entity.getCreateTime() );
        menuDTO.updateTime( entity.getUpdateTime() );

        return menuDTO.build();
    }

    @Override
    public SysMenu toEntity(MenuDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysMenu sysMenu = new SysMenu();

        sysMenu.setCreateTime( dto.getCreateTime() );
        sysMenu.setUpdateTime( dto.getUpdateTime() );
        sysMenu.setMenuId( dto.getMenuId() );
        sysMenu.setMenuName( dto.getMenuName() );
        sysMenu.setParentId( dto.getParentId() );
        sysMenu.setOrderNum( dto.getOrderNum() );
        sysMenu.setPath( dto.getPath() );
        sysMenu.setComponent( dto.getComponent() );
        sysMenu.setQuery( dto.getQuery() );
        sysMenu.setRouteName( dto.getRouteName() );
        sysMenu.setIsFrame( dto.getIsFrame() );
        sysMenu.setIsCache( dto.getIsCache() );
        sysMenu.setMenuType( dto.getMenuType() );
        sysMenu.setVisible( dto.getVisible() );
        sysMenu.setStatus( dto.getStatus() );
        sysMenu.setPerms( dto.getPerms() );
        sysMenu.setIcon( dto.getIcon() );

        return sysMenu;
    }

    @Override
    public List<MenuDTO> toDtoList(List<SysMenu> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MenuDTO> list = new ArrayList<MenuDTO>( entities.size() );
        for ( SysMenu sysMenu : entities ) {
            list.add( toDto( sysMenu ) );
        }

        return list;
    }
}

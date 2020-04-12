package com.mao.mapper.sys;

import com.mao.entity.sys.Client;
import com.mao.entity.sys.Permission;
import com.mao.entity.sys.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据请求
 * @author zongx at 2020/4/10 21:05
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByName(@Param("username") String username);

    /**
     * 根据client_id查询客户端数据
     * @param id client_id
     * @return 客户端数据
     */
    Client getClientById(@Param("id") String id);

    /**
     * 根据角色id查询权限列表
     * @param id 角色id
     * @return 权限列表
     */
    List<Permission> getPermissionByRoleId(@Param("id") long id);

}

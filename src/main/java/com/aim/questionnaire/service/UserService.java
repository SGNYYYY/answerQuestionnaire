package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
//import com.aim.questionnaire.config.shiro.SysUserService;
//import com.aim.questionnaire.config.shiro.entity.UserOnlineBo;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
//import com.alibaba.fastjson.JSONArray;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wln on 2018\8\9 0009.
 */
@Service
public class UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    //@Autowired
    //private SysUserService sysUserService;

    @Autowired
    private QuestionnaireEntityMapper questionnaireEntityMapper;

    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    public PageInfo<Map<String,Object>> queryUserList(Map<String,Object> map) {
        List<Map<String,Object>> userList = userEntityMapper.queryUserList(map);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(userList);
        return pageInfo;
    }

    /**
     * 创建用户的基本信息
     * @param map
     * @return
     * @throws ParseException
     */
    public int addUserInfo(Map<Object,Object> redisMap, String user) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        map.put("username",user);
        map.put("password",redisMap.get("password"));
        map.put("id",redisMap.get("id"));
        map.put("createdBy",redisMap.get("createdBy"));
        map.put("lastUpdatedBy",redisMap.get("lastUpdatedBy"));
        map.put("status",redisMap.get("status"));
        map.put("startTime",dateFormat.parse(redisMap.get("startTime").toString()));
        map.put("stopTime",dateFormat.parse(redisMap.get("stopTime").toString()));
        map.put("creationDate",dateFormat.parse(redisMap.get("creationDate").toString()));
        map.put("lastUpdateDate",dateFormat.parse(redisMap.get("lastUpdateDate").toString()));
        int result = userEntityMapper.addUserInfo(map);
        return result;
    }

    /**
     * 编辑用户的基本信息
     * @param map
     * @return
     */
    public int modifyUserInfo(Map<Object,Object> redisMap, String user) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        map.put("username",user);
        map.put("password",redisMap.get("password"));
        map.put("id",redisMap.get("id"));
        map.put("lastUpdatedBy",redisMap.get("lastUpdatedBy"));
        map.put("startTime",dateFormat.parse(redisMap.get("startTime").toString()));
        map.put("stopTime",dateFormat.parse(redisMap.get("stopTime").toString()));
        map.put("lastUpdateDate",dateFormat.parse(redisMap.get("lastUpdateDate").toString()));
        int result = userEntityMapper.modifyUserInfo(map);
        return result;
    }

    /**
     * 修改用户状态
     * @param map
     * @return
     * @throws ParseException
     */
    public int modifyUserStatus(Map<Object, Object> redisMap) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        map.put("id",redisMap.get("id"));
        map.put("lastUpdatedBy",redisMap.get("lastUpdatedBy"));
        map.put("lastUpdateDate",dateFormat.parse(redisMap.get("lastUpdateDate").toString()));
        map.put("status",redisMap.get("status"));
        int result = userEntityMapper.modifyUserStatus(map);
        return result;
    }

    /**
     * 根据id查询用户信息
     * @param userEntity
     * @return
     */
    public Map<String,Object> selectUserInfoById(UserEntity userEntity) {

        return null;
    }

    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    public UserEntity selectAllByName(String username) {
        UserEntity userEntity = userEntityMapper.selectAllByName(username);
        return userEntity;
    }
    /**
     * 删除用户信息
     * @param userEntity
     * @return
     */
    public int deteleUserInfoById(UserEntity userEntity) {
        userEntityMapper.deteleUserInfoById(userEntity);
        return 0;
    }
}

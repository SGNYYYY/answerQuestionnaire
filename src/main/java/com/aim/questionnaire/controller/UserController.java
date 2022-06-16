package com.aim.questionnaire.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.UserService;
import com.github.pagehelper.PageInfo;
import com.aim.questionnaire.common.utils.RedisUtil;

/**
 * Created by wln on 2018\8\9 0009.
 */
@RestController
@RequestMapping("/admin")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private RedisUtil redisUtil; 

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityMapper userEntityMapper;
   
    /**
     * 用户登录
     * @param map
     * @return
     */
    @RequestMapping(value="/userLogin",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
          
            List<UserEntity> hasUser = userEntityMapper.selectUserInfo(userEntity);
            if(CollectionUtils.isEmpty(hasUser) ) {
            	httpResponseEntity.setCode(Constans.EXIST_CODE);
            	httpResponseEntity.setData(null);
            	httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
            }else {
            	httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            	httpResponseEntity.setData(hasUser.get(0));
            	httpResponseEntity.setMessage(Constans.LOGIN_MESSAGE);
            }

        } catch (Exception e) {
            logger.info("userLogin 用户登录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryUserList",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryUserList(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        PageInfo<Map<String,Object>> pageInfo = userService.queryUserList(map);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(pageInfo);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        return httpResponseEntity;
    }
    /**
     * 创建用户的基本信息
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addUserInfo(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        Map<Object,Object> redisMap = redisUtil.getHashEntries(username);
        userService.addUserInfo(redisMap, username);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);

        return httpResponseEntity;
    }

    /**
     * 编辑用户的基本信息
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserInfo(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        Map<Object,Object> redisMap = redisUtil.getHashEntries(username);
        userService.modifyUserInfo(redisMap, username);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        return httpResponseEntity;
    }


    /**
     *  根据用户id查询用户基本信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/selectUserInfoById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity selectUserInfoById(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }



    /**
     * 修改用户状态
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/modifyUserStatus",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserStatus(@RequestBody Map<String,Object> map) throws ParseException {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        Map<Object,Object> redisMap = redisUtil.getHashEntries(username);
        userService.modifyUserStatus(redisMap);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        return httpResponseEntity;
    }
    /**
     *  删除用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/deleteUserInfoById",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deteleUserInfoById(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        userService.deteleUserInfoById(userEntity);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
        return httpResponseEntity;
    }


    /**
     * 用户没有权限
     * @return
     */
    @RequestMapping(value = "/error")
    public HttpResponseEntity logout() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        
        return httpResponseEntity;
    }
}

package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.service.RedisService;
import com.aim.questionnaire.service.UserService;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.RedisUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

@RestController
@RequestMapping("/redis")
public class RedisController {

    //private final RedisTemplate redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Resource
    private RedisUtil redisUtil; 

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserEntityMapper userEntityMapper;
    // 测试redis
    // @RequestMapping(value="/test",method= RequestMethod.POST, headers = "Accept=application/json")
    // public HttpResponseEntity test() {
    //     HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    //     Map<String,String> map = new HashMap<String,String>();
    //     map.put("1","one");
    //     map.put("2","two");
    //     redisUtil.add("test", map);
    //     httpResponseEntity.setData(1);
    //     return httpResponseEntity;
    // }
    @RequestMapping(value="/userLogin",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Map<Object,Object> resultMap = redisService.userLogin(userEntity);
        if(resultMap == null){
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
        }else {
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setData(resultMap);
            httpResponseEntity.setMessage(Constans.LOGIN_MESSAGE);
        }
        return httpResponseEntity;
    }


    /**
     * 查询用户列表（模糊搜索）
     * @param map
     * @return
     */
    @RequestMapping(value="/queryUserList",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity queryUserList(){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Set<String> keySet = redisUtil.getListKey("");
        List<Map<Object,Object>> userList = new ArrayList<Map<Object,Object>>();
        for (String key : keySet) {
            Map<Object,Object> map = redisUtil.getHashEntries(key);
            map.put("username",key);
            userList.add(map);
        }
        PageInfo<Map<Object,Object>> pageInfo = new PageInfo<Map<Object,Object>>(userList);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(pageInfo);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        return httpResponseEntity;
    }

    /**
     * 通过用户名查找用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value="/selectUserInfoByUserName",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity selectUserInfoByUserName(@RequestBody UserEntity userEntity){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = userEntity.getUsername();
        Map<Object,Object> map = redisUtil.getHashEntries(username);
        map.put("username",username);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setData(map);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        return httpResponseEntity;
    }

    /**
     * 创建用户的基本信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity addUserInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        if(map.get("username") != null) {
            String username = map.get("username").toString();
            if(redisUtil.hasKey(username)) {
                httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
                httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
            }else{
                Map<String,String> redisMap = new HashMap<String,String>();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String id = UUIDUtil.getOneUUID();
                redisMap.put("id",id);
                //密码
                String password = map.get("password").toString();
                redisMap.put("password",password);
                //创建时间
                Date date = DateUtil.getCreateTime();
                redisMap.put("creationDate",dateFormat.format(date));
                redisMap.put("lastUpdateDate",dateFormat.format(date));
                //创建人
                String user = map.get("createdBy").toString();
                redisMap.put("createdBy",user);
                redisMap.put("lastUpdatedBy",user);
                //前台传入的时间戳转换
                String startTimeStr = map.get("startTime").toString();
                String endTimeStr = map.get("stopTime").toString();
                Date startTime = DateUtil.getMyTime(startTimeStr);
                Date endTime = DateUtil.getMyTime(endTimeStr);
                redisMap.put("startTime",dateFormat.format(startTime));
                redisMap.put("stopTime",dateFormat.format(endTime));
                redisMap.put("status","1");
                redisUtil.add(username, redisMap);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
            }  
        }
        return httpResponseEntity;
    }

    /**
     * 编辑用户的基本信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserInfo(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = map.get("username").toString();
        //如果更新了用户名
        if(map.get("oldUserName") != null) {
            if(redisUtil.hasKey(username)) {
                httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
                httpResponseEntity.setMessage(Constans.USER_USERNAME_MESSAGE);
            }else{
                Map<Object,Object> oldMap = redisUtil.getHashEntries(map.get("oldUserName").toString());
                redisUtil.deleteByPrex(map.get("oldUserName").toString());
                Map<String,String> redisMap = new HashMap<String,String>();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                redisMap.put("id",oldMap.get("id").toString());
                //密码
                if(map.get("password")!=null){
                    String password = map.get("password").toString();
                    redisMap.put("password",password);
                }else{
                    redisMap.put("password",oldMap.get("password").toString());
                }

                //创建时间
                Date date = DateUtil.getCreateTime();
                redisMap.put("creationDate",oldMap.get("creationDate").toString());
                //更新时间
                redisMap.put("lastUpdateDate",dateFormat.format(date));
                //创建人
                String user = map.get("lastUpdatedBy").toString();
                redisMap.put("createdBy",oldMap.get("createdBy").toString());
                redisMap.put("lastUpdatedBy",user);
                //前台传入的时间戳转换
                String startTimeStr = map.get("startTime").toString();
                String endTimeStr = map.get("stopTime").toString();
                Date startTime = DateUtil.getMyTime(startTimeStr);
                Date endTime = DateUtil.getMyTime(endTimeStr);
                redisMap.put("startTime",dateFormat.format(startTime));
                redisMap.put("stopTime",dateFormat.format(endTime));
                redisMap.put("status",oldMap.get("status").toString());
                redisUtil.add(username, redisMap);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
            }  
        }else{
            Map<String,String> redisMap = new HashMap<String,String>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //密码
            if(map.get("password")!=null){
                String password = map.get("password").toString();
                redisMap.put("password",password);
            }

            //更新时间
            Date date = DateUtil.getCreateTime();
            redisMap.put("lastUpdateDate",dateFormat.format(date));
            //创建人
            String user = map.get("lastUpdatedBy").toString();
            redisMap.put("lastUpdatedBy",user);
            //前台传入的时间戳转换
            String startTimeStr = map.get("startTime").toString();
            String endTimeStr = map.get("stopTime").toString();
            Date startTime = DateUtil.getMyTime(startTimeStr);
            Date endTime = DateUtil.getMyTime(endTimeStr);
            redisMap.put("startTime",dateFormat.format(startTime));
            redisMap.put("stopTime",dateFormat.format(endTime));
            redisUtil.add(username, redisMap);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        }
        return httpResponseEntity;
    }

    /**
     * 修改用户状态
     * @param map
     * @return
     */
    @RequestMapping(value = "/modifyUserStatus",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity modifyUserStatus(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        if(map.get("username") != null) {
            String username = map.get("username").toString();
            Map<String,String> redisMap = new HashMap<String,String>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //更新时间
            Date date = DateUtil.getCreateTime();
            redisMap.put("lastUpdateDate",dateFormat.format(date));
            //创建人
            String user = map.get("lastUpdatedBy").toString();
            redisMap.put("lastUpdatedBy",user);
            redisMap.put("status",map.get("status").toString());
            redisUtil.add(username, redisMap);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.UPDATE_MESSAGE);
        }
        return httpResponseEntity;
    }
    /**
     *  删除用户信息
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/deleteUserInfoByUserName",method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity deteleUserInfoById(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        redisUtil.deleteByPrex(userEntity.getUsername());
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.DELETE_MESSAGE);
        return httpResponseEntity;
    }

}

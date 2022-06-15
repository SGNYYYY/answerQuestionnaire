package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.service.UserService;
import com.fasterxml.jackson.annotation.OptBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.common.utils.RedisUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

@RestController
@RequestMapping("/redis")
public class RedisController {

    //private final RedisTemplate redisTemplate;

    @Resource
    private RedisUtil redisUtil; 

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @RequestMapping(value="/test",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity test() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Map<String,String> map = new HashMap<String,String>();
        map.put("1","one");
        map.put("2","two");
        redisUtil.add("test", map);
        httpResponseEntity.setData(1);
        return httpResponseEntity;
    }
    @RequestMapping(value="/userLogin",method= RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username = userEntity.getUsername();
        boolean haskey = redisUtil.hasKey(username);
        if(!haskey){
            UserEntity resultUserEntity = userService.selectAllByName(username);
            Map<String,String> map = new HashMap<String,String>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("id",resultUserEntity.getId());
            map.put("password",resultUserEntity.getPassword());
            map.put("start_time",dateFormat.format(resultUserEntity.getStartTime()));
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("stop_time",dateFormat.format(resultUserEntity.getStopTime()));
            map.put("created_by",resultUserEntity.getCreatedBy());
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("creation_date",dateFormat.format(resultUserEntity.getCreationDate()));
            map.put("last_updated_by",resultUserEntity.getLastUpdatedBy());
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("last_update_date",dateFormat.format(resultUserEntity.getLastUpdateDate()));
            redisUtil.add(username, map);
        }
        //String result2 = redisUtil.getMapString("admin","password").toString();
        Map<Object,Object> map = redisUtil.getHashEntries(username);
        String password = map.get("password").toString();
        if(password.equals(userEntity.getPassword())){
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setData(map);
            httpResponseEntity.setMessage(Constans.LOGIN_MESSAGE);
        }else{
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
        }
        //httpResponseEntity.setData(map);
        return httpResponseEntity;
    }

}

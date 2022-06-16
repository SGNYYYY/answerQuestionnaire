package com.aim.questionnaire.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aim.questionnaire.common.utils.RedisUtil;
import com.aim.questionnaire.dao.entity.UserEntity;

@Service
public class RedisService {
    @Resource
    private RedisUtil redisUtil; 
    
    @Autowired
    private UserService userService;
    /**
     * 
     */
    public Map<Object,Object> userLogin(UserEntity userEntity){
        String username = userEntity.getUsername();
        boolean haskey = redisUtil.hasKey(username);
        if(!haskey){
            UserEntity resultUserEntity = userService.selectAllByName(username);
            Map<String,String> map = new HashMap<String,String>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if(resultUserEntity == null){
                return null;
            }
            map.put("id",resultUserEntity.getId());
            map.put("password",resultUserEntity.getPassword());
            map.put("startTime",dateFormat.format(resultUserEntity.getStartTime()));
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("stopTime",dateFormat.format(resultUserEntity.getStopTime()));
            map.put("createdBy",resultUserEntity.getCreatedBy());
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("creationDate",dateFormat.format(resultUserEntity.getCreationDate()));
            map.put("lastUpdatedBy",resultUserEntity.getLastUpdatedBy());
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("lastUpdateDate",dateFormat.format(resultUserEntity.getLastUpdateDate()));
            map.put("status",resultUserEntity.getStatus());
            redisUtil.add(username, map);
        }
        //String result2 = redisUtil.getMapString("admin","password").toString();
        Map<Object,Object> resultMap = redisUtil.getHashEntries(username);
        String password = resultMap.get("password").toString();
        if(password.equals(userEntity.getPassword())){
            // httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            // httpResponseEntity.setData(map);
            // httpResponseEntity.setMessage(Constans.LOGIN_MESSAGE);
            return resultMap;
        }else{
            // httpResponseEntity.setCode(Constans.EXIST_CODE);
            // httpResponseEntity.setData(null);
            // httpResponseEntity.setMessage(Constans.LOGIN_USERNAME_PASSWORD_MESSAGE);
            return null;
        }
    }
}

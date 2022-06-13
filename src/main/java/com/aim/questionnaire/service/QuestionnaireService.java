package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;

import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wln on 2018\8\6 0006.
 */
@Service
public class QuestionnaireService {

    @Autowired
    private QuestionnaireEntityMapper questionnaireEntityMapper;


    @Autowired
    private ProjectService projectService;

    /**
     * 添加项目
     * 
     * @param questionnaireEntity
     * @return
     */
    public int addQuestionnaire(QuestionnaireEntity questionnaireEntity, String user) {
        String id = UUIDUtil.getOneUUID();
        questionnaireEntity.setId(id);
        // 获取用户信息
        questionnaireEntity.setCreatedBy(user);
        questionnaireEntity.setLastUpdatedBy(user);
        // 获取当前时间
        Date date = DateUtil.getCreateTime();
        questionnaireEntity.setCreationDate(date);
        questionnaireEntity.setLastUpdateDate(date);

        int result = questionnaireEntityMapper.insertSelective(questionnaireEntity);
        return result;
    }

    /**
     * 修改问卷基本信息
     * 
     * @param questionnaireEntity
     * @return
     */
    public int modifyQuestionnaireInfo(QuestionnaireEntity questionnaireEntity, String user) {
        // 获取用户信息
        questionnaireEntity.setLastUpdatedBy(user);
        // 获取当前时间
        Date date = DateUtil.getCreateTime();
        questionnaireEntity.setLastUpdateDate(date);
        int result = questionnaireEntityMapper.modifyQuestionnaireInfo(questionnaireEntity);
        return result;
    }

    /**
     * 修改问卷基本设计
     * 
     * @param questionnaireEntity
     * @return
     */
    public int modifyQuestionnaire(HashMap<String, Object> map) {
        Date date = DateUtil.getCreateTime();
        map.put("endTime", date);
        //map.put("questionList", map.get("questionList"));
        int result =questionnaireEntityMapper.modifyQuestionnaire(map);
        System.out.print(result);
        return result;
    }

    /**
     * 修改问卷状态
     */
    public int modifyQuestionnaireStatus(HashMap<String, Object> map) {
        int result = questionnaireEntityMapper.modifyQuestionnaireStatus(map);
        return result;
    }

    /**
     * 根据问卷id查询问卷的详细信息
     * 
     * @param map
     * @return
     */
    public Map<String, String> queryQuestionnaireById(HashMap<String, Object> map) {
        Map<String, String> result = questionnaireEntityMapper.queryQuestionnaireById(map);
        return result;
    }
    /**
     * 删除项目
     * 
     * @param questionnaireEntity
     * @return
     */
    public int deleteQuestionnaireById(QuestionnaireEntity questionnaireEntity) {
        String questionnaireId = questionnaireEntity.getId();
        // System.out.println(questionnaireId);
        int result = questionnaireEntityMapper.deleteByPrimaryKey(questionnaireId);

        return result;
    }

    /**
     * 查询问卷列表
     * 
     * @param questionnaireEntity
     * @return
     */
    public List<Object> queryQuestionnaireList(HashMap<String, Object> map) {
        List<Object> resultList = new ArrayList<Object>();
        if("".equals(map.get("questionName").toString())){
            map.put("questionName", null);
        }
        List<Map<String,Object>> proResult = questionnaireEntityMapper.queryQuestionnaireList(map);
        for(Map<String,Object> proObj : proResult) {
            String projectId = proObj.get("projectId").toString();
            String projectName = projectService.queryProjectNameById(projectId);
            proObj.put("projectName",projectName);
            resultList.add(proObj);
        }
        return resultList;
    }

    /**
     * 查询问卷列表
     * 
     * @param questionnaireEntity
     * @return
     */
    public QuestionnaireEntity queryQuestionnaireAll(String id) {
        QuestionnaireEntity Result = questionnaireEntityMapper.selectByPrimaryKey(id);
        return Result;
    }

 
    /**
     * 根据项目id查询此项目下的全部问卷
     * @param projectId
     * @return
     */
    public List<Object> queryQuestionListByProjectId(String projectId) {
        List<Object> resultList = new ArrayList<Object>();
        List<Map<String,Object>> proResult = questionnaireEntityMapper.queryQuestionListByProjectId(projectId);
        for(Map<String,Object> proObj : proResult) {
            resultList.add(proObj);
        }
        return resultList;
    }
    /**
     * 查询历史问卷
     * 
     * @param questionnaireEntity
     * @return
     */
    public List<Object> queryHistoryQuestionnaire(HashMap<String, Object> map) {
        List<Object> resultList = new ArrayList<Object>();
        List<Map<String,Object>> proResult = questionnaireEntityMapper.queryHistoryQuestionnaire(map);
        for(Map<String,Object> proObj : proResult) {
            resultList.add(proObj);
        }
        return resultList;
    }
    /**
     * 查询问卷状态
     * 
     * @param questionnaireEntity
     * @return
     */
    public String queryQuestionnaireIsStopStatus(String questionId) {
        String questionStop = questionnaireEntityMapper.queryQuestionnaireIsStopStatus(questionId);
        return questionStop;
    }

    /**
     * 查询问卷模板
     * @param dataId
     * @return
     */
    public List<Map<String,Object>> queryQuestionnaireMould(String dataId) {
        //List<Object> resultList = new ArrayList<Object>();
        List<Map<String,Object>> proResult = questionnaireEntityMapper.queryQuestionnaireMould(dataId);
        return proResult;
    }
    /**
     * 查询当前用户创建的问卷
     * @param dataId
     * @return
     */
    public List<Object> queryAllQuestionnaireByCreated(Map<String,Object> map) {
        List<Object> resultList = new ArrayList<Object>();
        List<Map<String,Object>> proResult = questionnaireEntityMapper.queryAllQuestionnaireByCreated(map);
        for(Map<String,Object> proObj : proResult) {
            resultList.add(proObj);
        }
        return resultList;
    }
    /**
     * 获取答题结束提示语、和短信内容
     * @param id
     * @return
     */
    public QuestionnaireEntity queryQuestContextEnd(String id){
        QuestionnaireEntity questionnaireEntity = questionnaireEntityMapper.queryQuestContextEnd(id);
        return questionnaireEntity;
    }
    /**
     * 添加发送问卷方式
     * @param map
     * @return
     */
    public int addSendQuestionnaire(HashMap<String, Object> map){
        map.put("questionStop","1");
        // 获取当前时间
        Date date = DateUtil.getCreateTime();
        map.put("lastUpdateDate",date);
        map.put("releaseTime",date);
        int result = questionnaireEntityMapper.addSendQuestionnaire(map);
        return result;
    }
    /**
     * 
     */
    public int modifyHistoryQuestionnaireStatus(HashMap<String, Object> map){
        map.put("questionStop","1");
        String endTimeStr = map.get("endTime").toString();
        Date endTime = DateUtil.getMyTime(endTimeStr);
        map.put("endTime",endTime);
        int result = questionnaireEntityMapper.modifyHistoryQuestionnaireStatus(map);
        return result;
    }
}

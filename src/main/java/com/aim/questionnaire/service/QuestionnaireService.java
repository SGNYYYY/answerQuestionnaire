package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.dao.QuestionnaireEntityMapper;
import com.aim.questionnaire.dao.entity.QuestionnaireEntity;
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

}

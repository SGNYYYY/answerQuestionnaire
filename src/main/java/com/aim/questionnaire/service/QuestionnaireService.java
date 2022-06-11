package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
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

    /**
     * 添加项目
     * 
     * @param questionnaireEntity
     * @return
     */
    public int addQuestionnaire(QuestionnaireEntity questionnaireEntity, String user) {
        String id = UUIDUtil.getOneUUID();
        questionnaireEntity.setId(id);
        questionnaireEntity.setProjectId("test");

        int result = questionnaireEntityMapper.insertSelective(questionnaireEntity);
        return result;
    }

    /**
     * 修改项目
     * 
     * @param questionnaireEntity
     * @return
     */
    public int modifyQuestionnaireInfo(QuestionnaireEntity questionnaireEntity, String user) {
        return 0;
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
     * 查询项目列表
     * 
     * @param questionnaireEntity
     * @return
     */
    public List<Object> queryQuestionnaireList(QuestionnaireEntity questionnaireEntity) {
        return null;
    }

}

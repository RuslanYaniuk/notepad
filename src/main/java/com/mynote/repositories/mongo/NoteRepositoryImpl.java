package com.mynote.repositories.mongo;

import com.mynote.dto.note.NoteFindDTO;
import com.mynote.models.Note;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
@Component
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Note> find(NoteFindDTO noteFindDTO) {
        List<Criteria> criteriaList = new ArrayList<>();
        Criteria criteria = new Criteria();

        if (!StringUtils.isEmpty(noteFindDTO.getId())) {
            criteriaList.add(Criteria.where("id").is(noteFindDTO.getId()));
        }

        if (!StringUtils.isEmpty(noteFindDTO.getSubject())) {
            criteriaList.add(Criteria.where("subject").regex(noteFindDTO.getSubject()));
        }

        if (!StringUtils.isEmpty(noteFindDTO.getText())) {
            criteriaList.add(Criteria.where("text").regex(noteFindDTO.getText()));
        }

        criteria = criteria.orOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        return mongoTemplate.find(new Query(criteria), Note.class);
    }
}

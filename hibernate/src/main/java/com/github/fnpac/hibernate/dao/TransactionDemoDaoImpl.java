package com.github.fnpac.hibernate.dao;

import com.github.fnpac.hibernate.domain.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/6.
 */
@Service
public class TransactionDemoDaoImpl implements TransactionDemoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public List<PersonInfo> demo() {
        return jdbcTemplate.query("select * from person", (rs, rowNum) -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setId(rs.getInt("id"));
            personInfo.setName(rs.getString("name"));
            personInfo.setAge(rs.getInt("age"));
            personInfo.setEmail(rs.getString("email"));
            return personInfo;
        });
    }
}

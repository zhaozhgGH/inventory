package org.daming.inventory.dao.impl;

import org.daming.inventory.dao.BaseDao;
import org.daming.inventory.dao.GiftDao;
import org.daming.inventory.pojo.Gift;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 描述：
 *
 * @author daming
 * @date 2018-07-10 20:58
 */
@Repository
public class GiftDaoImpl extends BaseDao implements GiftDao {

    @Override
    public Gift get(int id) {
        return get("select id, code, name, num from gift where id =? ", new Object[]{id}, this::getGift);
    }

    @Override
    public KeyHolder create(Gift gift) {
        Assert.notNull(gift, "'gift' is required");
        String sql = "insert into gift(code, name, num) values (?,?, 0)";
        Object[] params = new Object[] { gift.getCode(), gift.getName(), gift.getNum() };
        return add(sql, params);
    }


    @Override
    public List<Gift> list() {
        return list("select id, code, name, num from gift", this::getGift);
    }

    @Override
    public List<Gift> like(String name, int pageNo, int pageSize) {
        String sql = "select id, code, name, num from gift";
        if (Objects.nonNull(name) && !"".equalsIgnoreCase(name.trim())) {
            sql += " where name like '%" + name + "%' ";
        }
        sql += " limit " + ((pageNo - 1) * pageSize) + "," + pageSize;
        return list(sql, this::getGift);
    }

    @Override
    public int delete(int id) {
        String sql ="delete * from gift where id = ?";
        return execute(sql, new Object[] { id } );
    }

    private Gift getGift(ResultSet rs) throws SQLException {
        while (rs.next()) {
            return new Gift()
                    .setId(rs.getInt("id"))
                    .setName(rs.getString("name"))
                    .setCode(rs.getString("code"))
                    .setNum(rs.getInt("num"));
        }
        return null;
    }

    private Gift getGift(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
        return new Gift()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setCode(rs.getString("code"))
                .setNum(rs.getInt("num"));
    }

    public GiftDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.setJdbcTemplate(jdbcTemplate);
    }
}

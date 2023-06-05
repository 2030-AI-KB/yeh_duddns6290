package com.member.ds;

import com.AppDao;
import com.dto.MemberDTO;

import java.sql.Connection;

import static com.common.JdbcUtil.*;

// 바가지와 두레박 얻는 객체
public class MemberDs {
    private Connection conn;

    private AppDao setDao() {
        AppDao dao = AppDao.getInstance();
        this.conn = getConnection();
        dao.setConnection(this.conn);
        return dao;
    }

    public int selectAccountCountByLoginId(String loginId) {
        AppDao dao = setDao();
        int count = dao.selectAccountCountByLoginId(loginId);
        close(conn);
        return count;
    }

    public boolean insertMemberInfo(MemberDTO dto) {
        AppDao dao = setDao();
        boolean isSuccess = dao.insertMemberInfo(dto);
        if (isSuccess) {
            commit(this.conn);
        } else {
            rollback(this.conn);
        }
        close(conn);

        return isSuccess;
    }
}

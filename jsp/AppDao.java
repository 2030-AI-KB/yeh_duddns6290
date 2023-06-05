package com;

import com.dto.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.common.JdbcUtil.close;

public class AppDao {
    private Connection conn;

    private AppDao() {

    }

    private static class LazyHolder {
        private static final AppDao INSTANCE = new AppDao();
    }

    public static AppDao getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public boolean insertName(String name) {
        PreparedStatement pstmt = null;
        int count = 0;

        try {
            pstmt = conn.prepareStatement("insert into my_name(name) values (?)");
            pstmt.setString(1, name);
            count = pstmt.executeUpdate(); // executeUpdate()는 실제로 테이블에 저장될 row의 갯수를 반환
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }

        return count > 0 ? true : false; // 삼항연산자
    }

    public String selectNameById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String name = null;

        try {
            pstmt = conn.prepareStatement("select name from my_name where id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) { // rs.next() :: 뽑아낸 데이터가 있으면 true 없으면 false
                name = rs.getString("name"); // rs.getString("컬럼명");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return name;
    }

    public int selectAccountCountByLoginId(String loginId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            pstmt = conn.prepareStatement("select count(*) from member where login_id = ? and leaved = false");
            pstmt.setString(1, loginId);
            rs = pstmt.executeQuery();
            while (rs.next()) { // rs.next() :: 뽑아낸 데이터가 있으면 true 없으면 false
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return count;
    }

    public boolean insertMemberInfo(MemberDTO dto) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count1 = 0;
        int count2 = 0;

        try {
            pstmt = conn.prepareStatement("insert into member_detail(name, mobile_no, birthday, email, zipcode, address, address_detail) value (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getMobileNo());
            pstmt.setObject(3, dto.getBirthday());
            pstmt.setString(4, dto.getEmail());
            pstmt.setString(5, dto.getZipcode());
            pstmt.setString(6, dto.getAddress());
            pstmt.setString(7, dto.getAddressDetail());
            count1 = pstmt.executeUpdate();
            if (count1 == 0) {
                close(rs);
                close(pstmt);
                return false;
            }

            rs = pstmt.getGeneratedKeys(); // 쿼리 실행 후 생성된 키 값 반환
            while (rs.next()) {
                dto.setMemberDetailId(rs.getInt(1));
            }

            pstmt = conn.prepareStatement("insert into member(login_id, password, member_detail_id) value (?,?,?)");
            pstmt.setString(1, dto.getLoginId());
            pstmt.setString(2, dto.getPassword());
            pstmt.setInt(3, dto.getMemberDetailId());
            count2 = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return count2 > 0 ? true : false;
    }
}

package life.lambert.community.community.mapper;

import life.lambert.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO USER(ACCOUNT_ID, NAME, TOKEN, GMT_CREATE, GMT_MODIFIED) VALUES(#{accountId}, #{name}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);

    @Select("SELECT * FROM USER WHERE token = #{token}")
    User findByToken(@Param("token") String token);
}

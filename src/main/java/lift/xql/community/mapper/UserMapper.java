package lift.xql.community.mapper;

import lift.xql.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId}, #{token}, #{gmtCreate},#{gmtModified})")
    void insert(User user);
}

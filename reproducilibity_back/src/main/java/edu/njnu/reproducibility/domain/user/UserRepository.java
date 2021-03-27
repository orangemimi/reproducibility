package edu.njnu.reproducibility.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/4 14:29
 * @modified By：
 * @version: 1.0.0
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    List<User> findByEmailLike(String value);
}

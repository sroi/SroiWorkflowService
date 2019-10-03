package benefitBountyService.dao;

import benefitBountyService.models.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String id);

    User findById(String id);

    User findByEmail(String emailId);

    List<User> findAll();

    User save(User user);

    List<User> saveAll(List<User> usersList);
}

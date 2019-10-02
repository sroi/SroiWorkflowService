package benefitBountyService.dao;

import benefitBountyService.models.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String id);

    User findById(String id);

    User findByEmail(String emailId);

    List<User> findAll();

    void save(User user);

    void saveAll(List<User> usersList);
}

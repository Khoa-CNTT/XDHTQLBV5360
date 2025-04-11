package vn.nhom24.bus_ticket_reservation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.nhom24.bus_ticket_reservation_system.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByFullName(String fullName);
    public User findByPhoneNumber(String phoneNumber);

    public User findByEmail(String email);
}

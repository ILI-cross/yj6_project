package org.zerock.b01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.User;
import org.zerock.b01.dto.UserDTO;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByMidAndMpw(String mid, String mpw);
}
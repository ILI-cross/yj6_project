package org.zerock.b01.repository;


import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.User;

@SpringBootTest
@Log4j2
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsert(){
        User user = User.builder()
                .mid("허승")
                .mpw("1234")
                .mname("허승")
                .build();
        userRepository.save(user);
    }
}

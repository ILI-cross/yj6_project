package org.zerock.b01.service;

import org.springframework.stereotype.Service;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.UserDTO;

@Service
public interface UserService {
    Long register(UserDTO userDTO);
    UserDTO login(String mid, String mpw);
}

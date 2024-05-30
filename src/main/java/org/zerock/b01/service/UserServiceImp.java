package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.User;
import org.zerock.b01.dto.UserDTO;
import org.zerock.b01.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{ //세이브하기 위해 사용

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Long register(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Long id = userRepository.save(user).getId();
        return id;
    }

    @Override
    public UserDTO login(String mid, String mpw) {
        Optional<User> result = userRepository.findByMidAndMpw(mid, mpw);
        User user = result.orElseThrow(() -> new NoSuchElementException("Invalid username or password"));
        return modelMapper.map(user, UserDTO.class);
    }

}

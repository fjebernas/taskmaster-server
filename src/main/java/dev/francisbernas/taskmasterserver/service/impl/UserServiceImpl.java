package dev.francisbernas.taskmasterserver.service.impl;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.mapper.UserMapper;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.mapEntitiesToDtos(userRepository.findAllNotDeleted());
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findByIdNotDeleted(userId);
        return optionalUser.map(UserMapper::mapEntityToDto).orElse(null);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getId() != null) {
            throw new RuntimeException(String.format("A user to create cannot have an id: %s", userDto.getId()));
        } else {
            User userEntity = UserMapper.mapDtoToEntity(userDto);
            // TODO: dynamic createdBy field - need Spring Security for this
            userEntity.setCreatedBy("admin");
            userEntity.setCreatedDate(LocalDateTime.now());
            User createdUser = userRepository.save(userEntity);
            return UserMapper.mapEntityToDto(createdUser);
        }
    }

    @Transactional
    @Override
    public boolean deleteUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findByIdNotDeleted(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // TODO: dynamic deletedBy field - need Spring Security for this
            userRepository.deleteById("admin", LocalDateTime.now(), user.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDto updateUserById(Long userId, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByIdNotDeleted(userId);
        if (optionalUser.isPresent()) {
            if (!userId.equals(userDto.getId())) {
                throw new RuntimeException(String.format("User id %s doesn't match given path variable id %s", userDto.getId(), userId));
            }
            User updatedUser = UserMapper.mapDtoToEntity(userDto);
            // TODO: dynamic deletedBy field - need Spring Security for this
            updatedUser.setLastModifiedBy("admin");
            updatedUser.setLastModifiedDate(LocalDateTime.now());
            User savedUpdatedUser = userRepository.save(updatedUser);
            return UserMapper.mapEntityToDto(savedUpdatedUser);
        } else {
            throw new RuntimeException(String.format("User with id %s doesn't exist", userId));
        }
    }
}

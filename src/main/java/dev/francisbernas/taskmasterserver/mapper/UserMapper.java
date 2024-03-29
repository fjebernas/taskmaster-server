package dev.francisbernas.taskmasterserver.mapper;

import dev.francisbernas.taskmasterserver.dto.UserDto;
import dev.francisbernas.taskmasterserver.entity.User;

import java.util.List;

public class UserMapper {
    public static UserDto mapEntityToDto(User userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPassword(userEntity.getPassword());
        return userDto;
    }

    public static List<UserDto> mapEntitiesToDtos(List<User> userEntities) {
        return userEntities.stream().map(UserMapper::mapEntityToDto).toList();
    }

    public static User mapDtoToEntity(UserDto userDto) {
        User userEntity = new User();
        userEntity.setId(userDto.getId());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }
}

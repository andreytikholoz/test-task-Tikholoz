package ua.task.test.users.mapper;

import org.springframework.stereotype.Component;
import ua.task.test.model.UserDTO;
import ua.task.test.users.entity.UserEntity;

@Component
public class UserMapper {
    public UserDTO map(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId().toString());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setAvatarBase64(userEntity.getAvatarBase64());
        userDTO.setPasswordHash(userEntity.getPasswordHash());
        return userDTO;
    }
}

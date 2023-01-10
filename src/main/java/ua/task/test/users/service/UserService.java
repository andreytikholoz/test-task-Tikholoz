package ua.task.test.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.task.test.model.Base64AvatarDTO;
import ua.task.test.model.UserDTO;
import ua.task.test.model.UserListDTO;
import ua.task.test.users.entity.UserEntity;
import ua.task.test.users.mapper.UserMapper;
import ua.task.test.users.repository.UserRepository;
import ua.task.test.users.utils.EmailValidator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) {
        EmailValidator.validate(userDTO.getEmail());
        UserEntity userEntity = createUserEntity(userDTO);
        UserEntity createdUserEntity = userRepository.save(userEntity);
        return userMapper.map(createdUserEntity);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        EmailValidator.validate(userDTO.getEmail());
        UserEntity userEntity = updateUserEntity(userDTO);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userMapper.map(updatedUserEntity);
    }

    public void uploadAvatar(String email, Base64AvatarDTO base64AvatarDTO) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setAvatarBase64(base64AvatarDTO.getBase64());
        userRepository.save(userEntity);
    }

    public UserListDTO getUserList(Integer pageNumber, Integer pageSize) {
        Page<UserEntity> userEntities = userRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<UserDTO> users = userEntities
                .stream()
                .map(userMapper::map)
                .collect(Collectors.toList());
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUsers(users);
        userListDTO.pageSize(pageSize);
        userListDTO.pageNumber(pageNumber);
        return userListDTO;
    }

    public boolean checkUserExistsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPasswordHash(email, password);
    }

    public UserDTO getUserById(String userId) {
        return userMapper.map(userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new IllegalStateException("User with id = [" + userId + "] not exist")));
    }

    private UserEntity createUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setAvatarBase64(userDTO.getAvatarBase64());
        userEntity.setPasswordHash(userDTO.getPasswordHash());
        return userEntity;
    }

    private UserEntity updateUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.fromString(userDTO.getId()));
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setAvatarBase64(userDTO.getAvatarBase64());
        userEntity.setPasswordHash(userDTO.getPasswordHash());
        return userEntity;
    }
}

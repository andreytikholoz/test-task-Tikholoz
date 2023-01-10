package ua.task.test.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import ua.task.test.api.UserApi;
import ua.task.test.model.Base64AvatarDTO;
import ua.task.test.model.UserDTO;
import ua.task.test.model.UserListDTO;
import ua.task.test.users.annotations.CurrentUserId;
import ua.task.test.users.service.UserService;

@RestController
public class UserController implements UserApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        LOGGER.info("Create user: email=[{}]", userDTO.getEmail());
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHORIZED_USER')")
    public ResponseEntity<UserDTO> authorizeUser(@CurrentUserId String id) {
        LOGGER.info("Authorize user: id=[{}]", id);
        String userEmail = getAuthorizedUserEmail();
        UserDTO user = userService.getUserById(id);
        if (userEmail.equals(user.getEmail())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHORIZED_USER')")
    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        LOGGER.info("Update user: email=[{}]", userDTO.getEmail());
        String userEmail = getAuthorizedUserEmail();
        UserDTO user = userService.getUserById(userDTO.getId());
        if (userEmail.equals(user.getEmail())) {
            return ResponseEntity.ok(userService.updateUser(userDTO));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHORIZED_USER')")
    public ResponseEntity<Void> uploadAvatar(Base64AvatarDTO base64AvatarDTO) {
        LOGGER.info("Upload user avatar");
        String userEmail = getAuthorizedUserEmail();
        userService.uploadAvatar(userEmail, base64AvatarDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_AUTHORIZED_ADMIN')")
    public ResponseEntity<UserListDTO> getUserList(Integer pageNumber, Integer pageSize) {
        LOGGER.info("Get user list");
        return ResponseEntity.ok(userService.getUserList(pageNumber, pageSize));
    }

    private String getAuthorizedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

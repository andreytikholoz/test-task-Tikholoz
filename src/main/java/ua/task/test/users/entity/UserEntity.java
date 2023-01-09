package ua.task.test.users.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "USER_ENTITY_T01")
public class UserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UUID")
    private UUID id;

    @Column(name = "EMAIL")
    private String email;

    @Lob
    @Column(name = "AVATAR_BASE64")
    private String avatarBase64;

    @Column(name = "PASSWORD_HASH")
    private String passwordHash;
}

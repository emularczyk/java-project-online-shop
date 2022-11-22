package com.candyshop.islodycze.model;

import com.candyshop.islodycze.model.Enum.Role;
import com.candyshop.islodycze.model.Enum.UserStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "users")
@ToString
@DynamicInsert
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String userPassword;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String verificationCode;

    @Enumerated(value = EnumType.STRING)
    private UserStatus verificationStatus;

    private int loyaltyPoints;

    @OneToMany(mappedBy = "userIdFk")
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();
}

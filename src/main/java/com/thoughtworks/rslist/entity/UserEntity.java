package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int vote;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventEntityList;
}

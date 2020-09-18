package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rs_event")
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String eventName;
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    @JsonProperty
    public UserEntity getUserEntity() {
        return userEntity;
    }
    @JsonIgnore
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}

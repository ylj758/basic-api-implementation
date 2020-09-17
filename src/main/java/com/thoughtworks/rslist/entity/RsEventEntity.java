package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private int userId;
    @JsonIgnore
    public int getUserId() {
        return userId;
    }
    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }


}

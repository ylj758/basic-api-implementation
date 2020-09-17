package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent implements Serializable {
    public interface RsEventDetail{}

    @NotEmpty
    @JsonView(RsEventDetail.class)//或者在userDto字段上使用JsonIgnore
    private String eventName;
    @NotEmpty
    @JsonView(RsEventDetail.class)
    private String keyword;
    @NotNull
    private int userId;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}

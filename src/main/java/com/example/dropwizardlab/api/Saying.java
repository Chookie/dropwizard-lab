package com.example.dropwizardlab.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Alison on 24/01/15
 */
public class Saying {
    @JsonProperty
    private long id;

    @JsonProperty
    @Length(max = 30)
    private String content;

    @JsonCreator
    public Saying(@JsonProperty("id") long id, @JsonProperty("content") String content) {
        this.id = id;
        this.content = content;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getContent() {
        return content;
    }
}

package com.documents;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Authorization {

    STUDENT("STUDENT") , ADMIN("ADMIN")  , NONE("???");

    private String name;

    Authorization(String name) {
        this.name = name;
    }


    public static Authorization fromString(String name) {

        for (Authorization status : Authorization.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }

        return null;

    }

    @Override
    public String toString() {
        return name;
    }
}
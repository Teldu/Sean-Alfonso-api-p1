package com.documents;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Authorization { STUDENT , ADMIN , NONE }

package com.edu.api;

public enum QueryParams {

    //Paginate
    PAGE("_page"),
    LIMIT("_limit"),

    //Sort
    SORT("_sort"),
    ORDER("_order"),

    //Slice
    START("_start"),
    END("_end"),

    //Operators
    GREATER_OR_EQUALS("_gte"),
    LESS_OR_EQUALS("_lte"),
    NOT_EQUALS("_ne"),
    LIKE("_like"),

    //Search
    QUERY("q"),

    //Relationships
    EMBED("_embed"),
    EXPAND("_expand");


    private final String paramName;

    public String value() {
        return paramName;
    }

    QueryParams(String paramName) {
        this.paramName = paramName;
    }
}

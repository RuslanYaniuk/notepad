package com.mynote.exceptions;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class SearchFieldsAreEmpty extends ExceptionWithParams {

    private static final long serialVersionUID = 1260764027787291226L;

    public SearchFieldsAreEmpty(String... args) {
        super("user.lookup.fieldsAreEmpty");
        super.args = args;
    }
}

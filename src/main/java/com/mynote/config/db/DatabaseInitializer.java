package com.mynote.config.db;

import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public interface DatabaseInitializer {

    void init() throws EmailAlreadyTakenException, LoginAlreadyTakenException;
}

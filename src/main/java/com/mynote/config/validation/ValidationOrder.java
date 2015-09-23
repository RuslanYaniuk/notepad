package com.mynote.config.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@GroupSequence({
        Default.class,
        ValidationGroupB.class,
        ValidationGroupC.class
})
public interface ValidationOrder {
}

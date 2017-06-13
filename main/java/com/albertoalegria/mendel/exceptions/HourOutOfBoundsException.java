package com.albertoalegria.mendel.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alberto Alegria
 */
public class HourOutOfBoundsException extends RuntimeException {
    private Log log = LogFactory.getLog(HourOutOfBoundsException.class);

    public HourOutOfBoundsException(int hour, int maxHour, Throwable cause) {
        log.error(getMessage(hour, maxHour), cause);
    }

    public HourOutOfBoundsException(int hour, int maxHour) {
        log.error(getMessage(hour, maxHour));
    }

    private String getMessage(int hour, int maxHour) {
        if (hour <= 0) {
            return  "Hour must be greater than 0";
        } else if (hour > maxHour){
            return "Hour must be equals or less than maximum hour [" + maxHour + "]";
        } else {
            return "All OK";
        }
    }
}

package com.james.ystechchallenge.core.enumeration;

/**
 * Enumeration of all the possible failure states recognized by the domain
 * @author james
 */
public enum FailureReason {
    EXISTING_REQUEST,
    INVALID_STATE_TRANSITION,
    SOURCE_ITEM_NOT_FOUND,
    MISSING_DATA
}

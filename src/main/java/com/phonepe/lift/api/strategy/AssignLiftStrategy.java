package com.phonepe.lift.api.strategy;

import com.phonepe.lift.api.models.Request;

/**
 * @author Pushpendra Pal
 */
public interface AssignLiftStrategy {
    boolean assignLift(Request request);
}

package com.phonepe.lift.api.models;

import com.phonepe.lift.api.common.Direction;

/**
 * @author Pushpendra Pal
 */
public class Request {
    private int sourceFloorNum;
    private int destinationFloorNum;
    private Direction direction;

    public Request(int sourceFloorNum, int destinationFloorNum) {
        this.sourceFloorNum = sourceFloorNum;
        this.destinationFloorNum = destinationFloorNum;
    }

    public int getSourceFloorNum() {
        return sourceFloorNum;
    }

    public int getDestinationFloorNum() {
        return destinationFloorNum;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}

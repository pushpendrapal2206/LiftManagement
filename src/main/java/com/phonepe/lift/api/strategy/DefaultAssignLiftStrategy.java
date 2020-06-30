package com.phonepe.lift.api.strategy;

import com.phonepe.lift.api.common.Direction;
import com.phonepe.lift.api.models.Lift;
import com.phonepe.lift.api.models.Request;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Pushpendra Pal
 */
public class DefaultAssignLiftStrategy implements AssignLiftStrategy {
    private List<Lift> liftList;

    public DefaultAssignLiftStrategy(List<Lift> liftList) {
        this.liftList = liftList;
    }

    @Override
    public boolean assignLift(final Request request) {
        boolean isAssigned = false;
        int minimumDistance = 10000;
        Lift minimumDistanceLift = null;
        for (Lift lift : this.liftList) {
            int distance = request.getSourceFloorNum() - lift.getCurrentFloor().get();
            if (lift.getDirection().equals(request.getDirection()) && minimumDistance > distance) {
                minimumDistance = distance;
                minimumDistanceLift = lift;
            }

            if (Direction.DOWN.equals(lift.getDirection()) && distance < 0 && minimumDistance >= Math.abs(distance)) {
                minimumDistance = distance;
                minimumDistanceLift = lift;
            }

            if (Direction.UP.equals(lift.getDirection()) && distance < 0 && minimumDistance >= Math.abs(distance)) {
                minimumDistance = distance;
                minimumDistanceLift = lift;
            }
        }
        if (minimumDistanceLift != null) {
            Queue<Integer> queue = null;
            if (Direction.UP.equals(request.getDirection())) {
                queue = new PriorityQueue<>();
            } else if (Direction.DOWN.equals(request.getDirection())) {
                queue = new PriorityQueue<>(Comparator.reverseOrder());
            }
            if (minimumDistanceLift.getDestinationFloorNums() == null) {
                minimumDistanceLift.setDestinationFloorNums(queue);
            }
            minimumDistanceLift.getDestinationFloorNums().add(request.getSourceFloorNum());
            minimumDistanceLift.getDestinationFloorNums().add(request.getDestinationFloorNum());
            isAssigned = true;
        }
        if (!isAssigned) {
            for (Lift lift : this.liftList) {
                if (lift.getDestinationFloorNums() == null) {
                    isAssigned = assignEmptyLift(lift, request);
                    break;
                } else if (lift.getDestinationFloorNums().isEmpty()) {
                    isAssigned = assignEmptyLift(lift, request);
                    break;
                } else if (lift.getDirection().equals(request.getDirection()) &&
                        request.getSourceFloorNum() >= lift.getCurrentFloor().get() &&
                        lift.getDestinationFloorNums().peek() >= request.getDestinationFloorNum()) {
                    lift.getDestinationFloorNums().add(request.getSourceFloorNum());
                    lift.getDestinationFloorNums().add(request.getDestinationFloorNum());
                    isAssigned = true;
                    break;
                }
            }
        }
        return isAssigned;
    }

    private boolean assignMinimumDistanceLift(List<Lift> lifts, Request request) {
        int minimumDistance = 10000;
        Lift minimumDistanceLift = null;
        for (Lift lift : lifts) {
            int distance = request.getSourceFloorNum() - lift.getCurrentFloor().get();
            if (minimumDistance > distance) {
                minimumDistance = distance;
                minimumDistanceLift = lift;
            }
        }
        if (minimumDistanceLift != null) {
            minimumDistanceLift.getDestinationFloorNums().add(request.getSourceFloorNum());
            minimumDistanceLift.getDestinationFloorNums().add(request.getDestinationFloorNum());
            return true;
        }
        return false;
    }

    private boolean assignEmptyLift(Lift lift, Request request) {
        if (lift.getCurrentFloor().get() < request.getSourceFloorNum()) {
            lift.setDirection(Direction.UP);
        } else if (lift.getCurrentFloor().get() > request.getSourceFloorNum()) {
            lift.setDirection(Direction.DOWN);
        } else {
            lift.setDirection(request.getDirection());
        }
        Queue<Integer> queue = null;
        if (Direction.UP.equals(request.getDirection())) {
            queue = new PriorityQueue<>();
        } else if (Direction.DOWN.equals(request.getDirection())) {
            queue = new PriorityQueue<>(Comparator.reverseOrder());
        }
        lift.setDestinationFloorNums(queue);
        lift.getDestinationFloorNums().add(request.getSourceFloorNum());
        lift.getDestinationFloorNums().add(request.getDestinationFloorNum());
        return true;
    }
}

package com.phonepe.lift.api;

import com.phonepe.lift.api.common.Direction;
import com.phonepe.lift.api.common.State;
import com.phonepe.lift.api.models.Floor;
import com.phonepe.lift.api.models.Lift;
import com.phonepe.lift.api.models.Request;
import com.phonepe.lift.api.strategy.AssignLiftStrategy;
import com.phonepe.lift.api.strategy.DefaultAssignLiftStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pushpendra Pal
 */
public class LiftController {
    private static LiftController liftController;
    private AssignLiftStrategy assignLiftStrategy;
    private List<Lift> lifts;
    private List<Floor> floors;
    private Queue<Request> requestQueue;


    private LiftController() {
        this.lifts = new ArrayList<>();
        this.floors = new ArrayList<>();
        this.requestQueue = new LinkedBlockingQueue<>();
    }

    public static LiftController getInstance() {
        if (liftController == null) {
            liftController = new LiftController();
        }
        return liftController;
    }

    public List<Lift> getLifts() {
        return lifts;
    }

    public void initializeLiftSystem(final int numOfLifts, final int numOfFloors) {
        for (int i = 1; i <= numOfLifts; i++) {
            Lift lift = new Lift("L" + i);
            lift.setCurrentFloor(new AtomicInteger(0));
            lift.setState(State.CLOSED);
            lift.setDirection(Direction.UP);
            this.lifts.add(lift);
        }

        this.assignLiftStrategy = new DefaultAssignLiftStrategy(this.lifts);
        for (int i = 0; i < numOfFloors; i++) {
            Floor floor = new Floor(i);
            this.floors.add(floor);
        }
    }

    public void processRequest(final Request request) {
        if (request.getDestinationFloorNum() - request.getSourceFloorNum() > 0) {
            request.setDirection(Direction.UP);
        } else {
            request.setDirection(Direction.DOWN);
        }
        requestQueue.add(request);
        pollForAssigningRequest(this.requestQueue);
    }

    private void pollForAssigningRequest(Queue<Request> requestQueue) {
        while (!requestQueue.isEmpty()) {
            if (this.assignLiftStrategy.assignLift(requestQueue.peek())) {
                requestQueue.poll();
            }
        }
    }

    public void start() {
        for (Lift lift : this.lifts) {
            new Thread(lift).start();
        }
    }
}

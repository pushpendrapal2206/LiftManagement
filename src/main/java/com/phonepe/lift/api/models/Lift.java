package com.phonepe.lift.api.models;

import com.phonepe.lift.api.common.Direction;
import com.phonepe.lift.api.common.State;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pushpendra Pal
 */
public class Lift implements Runnable {

    private String liftId;
    private State state;
    private Direction direction;
    private AtomicInteger currentFloor;
    private Queue<Integer> destinationFloorNums;
    private AtomicInteger cost;

    public Lift(String liftId) {
        this.liftId = liftId;
        this.cost = new AtomicInteger(0);
    }

    public String getLiftId() {
        return liftId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public AtomicInteger getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(AtomicInteger currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Queue<Integer> getDestinationFloorNums() {
        return destinationFloorNums;
    }

    public void setDestinationFloorNums(Queue<Integer> destinationFloorNums) {
        this.destinationFloorNums = destinationFloorNums;
    }

    public AtomicInteger getCost() {
        return cost;
    }

    public void setCost(AtomicInteger cost) {
        this.cost = cost;
    }

    @Override
    public void run() {
        while (true) {
            if (this.state.equals(State.OPEN)) {
                try {
                    this.cost.addAndGet(1);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.destinationFloorNums != null && !this.destinationFloorNums.isEmpty() && this.destinationFloorNums.peek() == this.currentFloor.get()) {
                this.state = State.OPEN;
                this.destinationFloorNums.poll();
            } else {
                this.state = State.CLOSED;
            }
            if (this.destinationFloorNums != null && !this.destinationFloorNums.isEmpty() && this.destinationFloorNums.peek() >= this.currentFloor.get()) {
                this.direction = Direction.UP;
            } else {
                this.direction = Direction.DOWN;
            }
            System.out.println(this.liftId + "-->" + this.getCurrentFloor().get() + " " + this.state.name());
            if (this.direction != null && this.direction.equals(Direction.UP) && !this.destinationFloorNums.isEmpty()) {
                if (!this.state.equals(State.OPEN)) {
                    this.currentFloor.addAndGet(1);
                    this.cost.addAndGet(1);
                }
            } else if (this.direction != null && this.destinationFloorNums != null && !this.destinationFloorNums.isEmpty()) {
                if (!this.state.equals(State.OPEN)) {
                    this.currentFloor.addAndGet(-1);
                    this.cost.addAndGet(1);
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

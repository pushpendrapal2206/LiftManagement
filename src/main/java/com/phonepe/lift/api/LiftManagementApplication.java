package com.phonepe.lift.api;

import com.phonepe.lift.api.models.Lift;
import com.phonepe.lift.api.models.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Pushpendra Pal
 */
public class LiftManagementApplication {

    public static void main(String[] args) throws IOException {
        try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            processInput(stdin);
        }
        for (Lift lift : LiftController.getInstance().getLifts()) {
            System.out.println(lift.getLiftId() + ":" + " " + lift.getCost().get() + " SECONDS");
        }
    }

    private static void processInput(BufferedReader stdin) throws IOException {
        String input;
        int numberOfLifts = 0;
        int numberOfFloors;
        int i = 0;
        while (((input = stdin.readLine()) != null && input.length() != 0)) {
            if (input.equalsIgnoreCase("stop")) {
                break;
            } else {
                if (i == 0) {
                    numberOfLifts = Integer.parseInt(input.split(" ")[3]);
                } else if (i == 1) {
                    numberOfFloors = Integer.parseInt(input.split(" ")[3]);
                    LiftController.getInstance().initializeLiftSystem(numberOfLifts, numberOfFloors);
                    LiftController.getInstance().start();
                } else {
                    Request request = new Request(Integer.parseInt(input.split(" ")[0]),
                            Integer.parseInt(input.split(" ")[1]));
                    LiftController.getInstance().processRequest(request);
                }
            }
            i++;
        }
    }
}


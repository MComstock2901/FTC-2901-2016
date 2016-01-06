package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class LegacyOpMode extends OpMode {

    float armUpSpeed = 0.5f;
    float armDownSpeed = -0.1f;

    float left = 0f;
    float right = 0f;

    double armPower = 0;

    boolean childLock = false;
    boolean childMode = false;
    // position of the claw servo
    double handPosition = 0.5;

    // amount to change the claw servo position by
    double handDelta = 0.01;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorArm;
    Servo hand;

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_right");
        motorLeft = hardwareMap.dcMotor.get("motor_left");
        motorArm = hardwareMap.dcMotor.get("motor_arm");
        hand = hardwareMap.servo.get("servo_hand");
    }

    @Override
    public void init_loop() {
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        if (gamepad2.y){
            childLock = !childLock;
            if (childLock) {
                telemetry.addData("ChildLock: ", "Activated!");
            }else {
                telemetry.addData("ChildLock: ", "Deactivated!");
            }
        }

        if (gamepad2.a){
            childMode = !childMode;
            if (childMode){
                telemetry.addData("ChildMode: ", "Activated!");
            }else {
                telemetry.addData("ChildMode: ", "Deactivated!");
            }
        }


        if (childMode) {
            if (gamepad2.right_stick_y == 0 && gamepad2.left_stick_y == 0 && !childLock){
                if (gamepad1.right_stick_x == 0) {
                    left = gamepad1.left_stick_x;
                    right = -gamepad1.left_stick_x;
                }else{
                    left = gamepad1.right_stick_x;
                    right = -gamepad1.right_stick_x;
                }
            }
        }else{
            if (gamepad2.right_stick_y == 0 && !childLock) {
                left = gamepad1.right_stick_y;
            } else {
                left = gamepad2.right_stick_y;
            }

            if (gamepad2.left_stick_y == 0 && !childLock) {
                right = gamepad1.left_stick_y;
            } else {
                right = gamepad2.left_stick_y;
            }
        }

//
        if (gamepad2.dpad_up){
            armPower = armUpSpeed;
//                               add  "&& lower" to the if statement to stop motor if it is below threshhold
        }else if (gamepad2.dpad_down){
            armPower = armDownSpeed;
        }else if (gamepad1.dpad_up && !childLock){
            armPower = armUpSpeed;
        }else if (gamepad1.dpad_down && !childLock){
            armPower = armDownSpeed;
        }else{
            armPower = 0;
        }


        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);
        motorArm.setPower(armPower);

        // update the position of the claw
        if (gamepad2.b) {
            handPosition -= handDelta;
            if (handPosition < 0.5){
                handPosition = 0.5;
            }
        }else if (gamepad1.b && !childLock){
            handPosition -= handDelta;
            if (handPosition < 0.5){
                handPosition = 0.5;
            }
        }

        if (gamepad2.x) {
            handPosition += handDelta;
            if (handPosition > 0.87){
                handPosition = 0.87;
            }
        }else if (gamepad1.x && !childLock){
            handPosition += handDelta;
            if (handPosition > 0.87){
                handPosition = 0.87;
            }
        }

        // clip the position values so that they never exceed 0..1
        handPosition = Range.clip(handPosition, 0, 1);

        // write position values to the wrist and claw servo
        //wrist.setPosition(wristPosition);
        hand.setPosition(handPosition);



    }

    // If the device is in either of these two modes, the op mode is allowed to write to the HW.
}

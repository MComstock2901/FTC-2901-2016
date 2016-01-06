package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.ftcrobotcontroller.opmodes.DriveFunctions;
/**
 * Created by chavezm17060 on 11/1/2015.
 */

public class TeleOp2016 extends OpMode {
    DcMotor rightDrive, leftDrive, armLiftMotor, armExtMotor;
    int lastRead;
    public void init() {
        telemetry.addData("1", "Robot Initializing");
        rightDrive = hardwareMap.dcMotor.get("2");
        leftDrive = hardwareMap.dcMotor.get("1");
        //armLiftMotor = hardwareMap.dcMotor.get("3");
        //armExtMotor = hardwareMap.dcMotor.get("4");
        //leftDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //rightDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //armLiftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //armExtMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        // armMotor = hardwareMap.dcMotor.get("arm");
        // lServo = hardwareMap.servo.get("servo");
        //rightMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        // leftMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        lastRead = rightDrive.getCurrentPosition();
    }

    float lPower;
    float rPower;
    int currentPos = 0;
    int speedReduce = 1;
    public void loop() {
        if (lastRead > 1000 && rightDrive.getCurrentPosition() < 400) {

            currentPos += ((1440 - lastRead) + rightDrive.getCurrentPosition());
        } else { // adds the difference between last enceder read and current read to current pos
            currentPos += (rightDrive.getCurrentPosition() - lastRead);
                lastRead = rightDrive.getCurrentPosition();
        }
        if (Math.abs(gamepad1.left_stick_y) > .15) {
            lPower = -gamepad1.left_stick_y / speedReduce;
        } else {
            lPower = 0;
        }
        if (Math.abs(gamepad1.right_stick_y) > .15) {
            rPower = -gamepad1.right_stick_y /speedReduce;
        } else {
            rPower = 0;
        }
        if (!gamepad1.b && !gamepad1.a) {
            //armLiftMotor.setPower(0);
        }
        if (gamepad1.left_bumper || gamepad2.left_bumper){
            speedReduce = 3;
        }else{
            speedReduce = 1;
        }
        if (gamepad1.b) {
            //armLiftMotor.setPower(.4);
        }
        if (gamepad1.a) {
            //armLiftMotor.setPower(-.4);
        }
        if ( !gamepad1.x && !gamepad1.y)
        {
            //armExtMotor.setPower(0);
        }
        if (gamepad1.x) {
            //armExtMotor.setPower(.3);
        }
        if (gamepad1.y) {
            //armExtMotor.setPower(-.3);
        }
        rightDrive.setPower(rPower);
        leftDrive.setPower(lPower);
        int currentEncoders = rightDrive.getCurrentPosition();
        telemetry.addData("RightPower = ", rPower);
        telemetry.addData("LeftPower = ", lPower);
        telemetry.addData("rightPos = ", rightDrive.getCurrentPosition());
        telemetry.addData("rightModde", rightDrive.getChannelMode());
        telemetry.addData("leftPos = ", leftDrive.getCurrentPosition());

    }

}

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by chavezm17060 on 11/1/2015.
 */

public class TeleOp2016New extends OpMode {
    DcMotor rightDrive, leftDrive, armLiftMotor, armExtMotor;
    Servo handServo;
    int lastRead;
    public void init() {
        telemetry.addData("1", "Robot Initializing");
        rightDrive = hardwareMap.dcMotor.get("2");
        leftDrive = hardwareMap.dcMotor.get("1");
        armLiftMotor = hardwareMap.dcMotor.get("3");
        handServo = hardwareMap.servo.get("s1");
        //armExtMotor = hardwareMap.dcMotor.get("4");
        telemetry.addData("runMode = ", leftDrive.getChannelMode());


        // armLiftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //armExtMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        armLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        // armMotor = hardwareMap.dcMotor.get("arm");
        // lServo = hardwareMap.servo.get("servo");
        //rightMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        // leftMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    float lPower;
    float rPower;
    int currentPos = 0;
    int speedReduce = 1;
    public void loop() {

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
            armLiftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            armLiftMotor.setPower(0);
        }
        if (gamepad1.left_bumper || gamepad2.left_bumper){
            speedReduce = 3;
        }else{
            speedReduce = 1;
        }
        if (gamepad1.b) {
            armLiftMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            armLiftMotor.setPower(.4);
        }
        if (gamepad1.a) {
            armLiftMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            armLiftMotor.setPower(-.1);
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
        if (gamepad1.left_bumper && handServo.getPosition() < .95){
            handServo.setPosition(handServo.getPosition() + .01);
        }
        if (gamepad1.right_bumper && handServo.getPosition() > .05){
            handServo.setPosition(handServo.getPosition() - .01);
        }
        rightDrive.setPower(rPower);
        leftDrive.setPower(lPower);
        double currentEncoders1 = rightDrive.getCurrentPosition();
        double currentEncoders2 = leftDrive.getCurrentPosition();
        double currentEncoders3 = armExtMotor.getCurrentPosition();
        double currentEncoders4 = armLiftMotor.getCurrentPosition();


        telemetry.addData("RightPower = ", rPower);
        telemetry.addData("LeftPower = ", lPower);
        telemetry.addData("rightPos = ", currentEncoders1);
        telemetry.addData("rightModde", rightDrive.getChannelMode());
        telemetry.addData("leftPos = ", currentEncoders2);
        telemetry.addData("ArmExtPos = ", currentEncoders3);
        telemetry.addData("ArmLiftPos = ", currentEncoders4);

    }

}

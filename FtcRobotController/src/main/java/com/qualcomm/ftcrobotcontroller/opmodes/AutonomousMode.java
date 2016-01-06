package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by michaelcomstock on 11/22/15.
 */
public class AutonomousMode extends LinearOpMode {
    DcMotor leftDrive;
    DcMotor rightDrive;
    public void runOpMode() throws InterruptedException {
        telemetry.addData("1", "Robot Initializing");
        rightDrive = hardwareMap.dcMotor.get("2");
        leftDrive = hardwareMap.dcMotor.get("1");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            while (time > 10) {


                while (time < 11) {
                    leftDrive.setPower(.6);
                    rightDrive.setPower(.6);
                }
                while (time < 13) {
                    leftDrive.setPower(-.6);
                    rightDrive.setPower(.6);
                }
                while (time < 14) {
                    leftDrive.setPower(.6);
                    rightDrive.setPower(.6);
                }
                while (time < 15) {
                    leftDrive.setPower(-.6);
                    rightDrive.setPower(.6);

                }
                while (time < 19) {
                    leftDrive.setPower(.6);
                    rightDrive.setPower(.6);
                }
                if (time > 19) {
                    rightDrive.setPower(0);
                    leftDrive.setPower(0);
                }

            }
        }
    }
}

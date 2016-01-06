/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class LegacyOpModeTracking extends OpMode {

    // position of the claw servo
    double handPosition;

    // amount to change the claw servo position by
    double handDelta = 0.01;

    // position of the wrist servo
    double wristPosition;

    // amount to change the wrist servo position by
    double wristDelta = 0.01;

    //tracks the arm movement from 0 to 1 (1 is the end of the rotation)
    int armPosition = 0;

    DcMotorController.DeviceMode devMode;
    DcMotorController driveController;
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorArm;

    Servo hand;
    //Servo wrist;

    int numOpLoops = 1;

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_right");
        motorLeft = hardwareMap.dcMotor.get("motor_left");
        motorArm = hardwareMap.dcMotor.get("motor_arm");
        hand = hardwareMap.servo.get("servo_hand");
        //hand.scaleRange();
        //wrist = hardwareMap.servo.get("servo_1"); // channel 1

        driveController = hardwareMap.dcMotorController.get("drive_controller");
    }

    /*
     * Code that runs repeatedly when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init_loop()
     */
    @Override
    public void init_loop() {

        devMode = DcMotorController.DeviceMode.WRITE_ONLY;

        motorRight.setDirection(DcMotor.Direction.REVERSE);
        //motorLeft.setDirection(DcMotor.Direction.REVERSE);

        // set the mode
        // Nxt devices start up in "write" mode by default, so no need to switch device modes here.

        /*
        motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorArm.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        */


        wristPosition = 0.6;
        handPosition = 0.5;
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {

        // The op mode should only use "write" methods (setPower, setChannelMode, etc) while in
        // WRITE_ONLY mode or SWITCHING_TO_WRITE_MODE
        if (allowedToWrite()) {
    /*
     * Gamepad 1
     *
     * Gamepad 1 controls the motors via the left stick, and it controls the wrist/claw via the a,b,
     * x, y buttons
     */

            if (gamepad1.dpad_left) {
                // Nxt devices start up in "write" mode by default, so no need to switch modes here.
                //motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
                //motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
            if (gamepad1.dpad_right) {
                // Nxt devices start up in "write" mode by default, so no need to switch modes here.
                //motorLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                //motorRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            }

            // throttle:  left_stick_y ranges from -1 to 1, where -1 is full up,  and 1 is full down
            // direction: left_stick_x ranges from -1 to 1, where -1 is full left and 1 is full right
            float throttle = gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;
            float armPower;

            if (gamepad1.dpad_up){
                armPower = 0.5f;

            }else if (gamepad1.dpad_down){
                armPower = -0.05f;
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

            // update the position of the wrist
            if (gamepad1.a) {
                wristPosition -= wristDelta;
            }

            if (gamepad1.b) {
                wristPosition += wristDelta;
            }

            // update the position of the claw
            if (gamepad1.y) {
                handPosition -= handDelta;
                if (handPosition < 0.5){
                    handPosition = 0.5;
                }
            }

            if (gamepad1.x /* && handPosition <= 0.87*/) {
                handPosition += handDelta;
                if (handPosition > 0.87){
                    handPosition = 0.87;
                }
            }

            // clip the position values so that they never exceed 0..1
            wristPosition = Range.clip(wristPosition, 0, 1);
            handPosition = Range.clip(handPosition, 0, 1);

            // write position values to the wrist and claw servo
            //wrist.setPosition(wristPosition);
            hand.setPosition(handPosition);

    /*
     * Gamepad 2
     *
     * Gamepad controls the motors via the right trigger as a throttle, left trigger as reverse, and
     * the left stick for direction. This type of control is sometimes referred to as race car mode.
     */

            // we only want to process gamepad2 if someone is using one of it's analog inputs. If you always
            // want to process gamepad2, remove this check
            if (gamepad2.atRest() == false) {

                // throttle is taken directly from the right trigger, the right trigger ranges in values from
                // 0 to 1
                throttle = gamepad2.right_trigger;

                // if the left trigger is pressed, go in reverse
                if (gamepad2.left_trigger != 0.0) {
                    throttle = -gamepad2.left_trigger;
                }

                // assign throttle to the left and right motors
                right = throttle;
                left = throttle;

                // now we need to apply steering (direction). The left stick ranges from -1 to 1. If it is
                // negative we want to slow down the left motor. If it is positive we want to slow down the
                // right motor.
                if (gamepad2.left_stick_x < 0) {
                    // negative value, stick is pulled to the left
                    left = left * (1 + gamepad2.left_stick_x);
                }
                if (gamepad2.left_stick_x > 0) {
                    // positive value, stick is pulled to the right
                    right = right * (1 - gamepad2.left_stick_x);
                }

                // write the values to the motor. This will over write any values placed while processing gamepad1
                motorRight.setPower(right);
                motorLeft.setPower(left);
            }
        }

        // To read any values from the NXT controllers, we need to switch into READ_ONLY mode.
        // It takes time for the hardware to switch, so you can't switch modes within one loop of the
        // op mode. Every 17th loop, this op mode switches to READ_ONLY mode, and gets the current power.


        if (numOpLoops % 200 == 0){
            driveController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            devMode = driveController.getMotorControllerDeviceMode();

            if (driveController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

                // Update the reads after some loops, when the command has successfully propagated through.
                telemetry.addData("left motor", motorLeft.getPower());
                telemetry.addData("right motor", motorRight.getPower());

                // Only needed on Nxt devices, but not on USB devices
                driveController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                devMode = driveController.getMotorControllerDeviceMode();

                // Reset the loop
                numOpLoops = 1;
            }
            // Note: If you are using the NxtDcMotorController, you need to switch into "read" mode
            // before doing a read, and into "write" mode before doing a write. This is because
            // the NxtDcMotorController is on the I2C interface, and can only do one at a time. If you are
            // using the USBDcMotorController, there is no need to switch, because USB can handle reads
            // and writes without changing modes. The NxtDcMotorControllers start up in "write" mode.
            // This method does nothing on USB devices, but is needed on Nxt devices.

        }



        // Every 17 loops, switch to read mode so we can read data from the NXT device.
        // Only necessary on NXT devices.


        // Update the current devMode

        numOpLoops++;

        telemetry.addData("Hand Servo", "Position:" + handPosition );
        telemetry.addData("Arm Motor", "Position:" + armPosition);

        telemetry.addData("Mode:", driveController.getMotorControllerDeviceMode().toString());
    }

    // If the device is in either of these two modes, the op mode is allowed to write to the HW.
    private boolean allowedToWrite(){
        return (devMode == DcMotorController.DeviceMode.WRITE_ONLY);
    }

    private boolean allowedToRead (){
        return (devMode == DcMotorController.DeviceMode.READ_ONLY);
    }
}

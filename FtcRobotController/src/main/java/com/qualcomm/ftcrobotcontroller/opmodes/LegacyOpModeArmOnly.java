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
public class LegacyOpModeArmOnly extends OpMode {

    float armUpSpeed = 0.5f;
    float armDownSpeed = -0.1f;
    int armPosition = 0;
    double armPowerRead = 0;

    DcMotorController.DeviceMode devMode;
    DcMotorController armController;
    DcMotor motorArm;

    int upperThresh = 0;
    int lowerThresh = 0;

    boolean upper;
    boolean lower;

    int numOpLoops = 1;
    int lastReadLoop = 0;

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {
        motorArm = hardwareMap.dcMotor.get("motor_arm");
        armController = hardwareMap.dcMotorController.get("arm_controller");

        //motorArm.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorArm.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    /*
     * Code that runs repeatedly when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init_loop()
     */
    @Override
    public void init_loop() {

        devMode = DcMotorController.DeviceMode.WRITE_ONLY;

        // set the mode
        // Nxt devices start up in "write" mode by default, so no need to switch device modes here.
        //motorArm.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorArm.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        devMode = armController.getMotorControllerDeviceMode();
        // The op mode should only use "write" methods (setPower, setChannelMode, etc) while in
        // WRITE_ONLY mode or SWITCHING_TO_WRITE_MODE
        if (allowedToWrite()) {

            //end race controls

            float armPower;
 //   add  "&& upper" to the if statement to stop motor if it is above threshhold
            if (gamepad1.right_stick_y < 0){
                armPower = armUpSpeed;
//    add  "&& lower" to the if statement to stop motor if it is below threshhold
            }else if (gamepad1.right_stick_y > 0){
                armPower = armDownSpeed;
            }else{
                armPower = 0;
            }
            telemetry.addData("Arm Power = ", armPower);
            motorArm.setPower(armPower);
            numOpLoops++;
            if (numOpLoops % 200 == 0) {
                armController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            }
        } else if(allowedToRead()){
            lastReadLoop = numOpLoops;
            armPosition = motorArm.getCurrentPosition();
            //armPosition = armController.getMotorCurrentPosition(1);
            armPowerRead = motorArm.getPower();
            armController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        } else {

        }

        telemetry.addData("Mode: ", devMode);
        telemetry.addData("opLoops: ", numOpLoops);


        // Update the current devMode

        telemetry.addData("last read: ", lastReadLoop);
        telemetry.addData("Arm Motor Position: ",  armPosition);
        telemetry.addData("Arm Power:", armPowerRead);
    }

    // If the device is in either of these two modes, the op mode is allowed to write to the HW.
    private boolean allowedToWrite(){
        return (devMode == DcMotorController.DeviceMode.WRITE_ONLY);
    }

    private boolean allowedToRead (){
        return (devMode == DcMotorController.DeviceMode.READ_ONLY);
    }
}

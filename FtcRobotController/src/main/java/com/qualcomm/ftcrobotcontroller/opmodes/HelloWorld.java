package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by michaelcomstock on 10/24/15.
 */
public class HelloWorld extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Debug:", "HELLOWORLD!!!");
    }

    @Override
    public void loop() {

    }
}

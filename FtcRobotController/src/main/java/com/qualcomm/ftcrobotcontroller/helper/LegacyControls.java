package com.qualcomm.ftcrobotcontroller.helper;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Legacy controller for controlling a robot with motorized arm that has a servo hand on the end
 * uses a single gamepad
 * wheels: joystick y-values controls each wheel
 * arm: a/b controls up/down of the motor
 * hand: x/y controls up/down of the servo
 */
public class LegacyControls extends Controls {
    public static final int ID_MOTOR_ARM = R.string.legacy_motor_arm;
    public static final int ID_SERVO_WRIST = R.string.legacy_servo_wrist; // not currently used
    public static final int ID_SERVO_HAND = R.string.legacy_servo_hand;

    public LegacyControls(OpMode opMode) {
        super(opMode);
    }

    @Override
    public float getValue(int key, float defaultValue) {
        switch (key) {
            case (ID_MOTOR_ARM):
                return getArmValue();
            case (ID_SERVO_HAND):
                return getHandValue();
            default:
                return super.getValue(key, defaultValue);
        }
    }

    /**
     * The Left Wheel Value is equal to the y position of the left joystick
     * @return value from -1 to 1
     */
    public float getLeftWheelValue() {
        return 0;
    }

    /**
     * The Right Wheel Value is equal to the y position of the left joystick
     * @return value from -1 to 1
     */
    public float getRightWheelValue() {
        return 0;
    }

    /**
     * Hand value is -1 if y is pressed, 1 if x is pressed, else is 0
     * @return -1,0,or 1 depending of state of x and y
     */
    public float getHandValue() {
        return 0;
    }

    /**
     * Arm value is -1 if a is pressed, 1 if b is pressed, else is 0
     * @return -1,0,or 1 depending of state of a and b
     */
    public float getArmValue() {
        return 0;
    }
}

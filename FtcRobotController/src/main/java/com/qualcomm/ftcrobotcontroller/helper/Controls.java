package com.qualcomm.ftcrobotcontroller.helper;

import android.content.Context;
import android.content.res.Resources;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Wrapper around both Gamepads
 *
 * This allows separation of the logic for reading the Gamepads from that of performing an opMode
 * This will also allow multiple opModes to share a single controller setup
 *
 * Example:
 *      assume we want to control a motor (called arm) with buttons x and y.
 *      let arm_id be the resource id of the motor (ie R.string.legacy_motor_arm)
 *      when we press x, it should turn in the positive direction, y in the negative, else no movement.
 *      so getValue(arm_id) should check the x and y values on mGamepad1 and return -1,1,or 0 accordingly.
 *      after which, the opMode will use that value to set the power of the motor based on that value
 *
 * This class should only be reading and returning Gamepad values.
 * Any interpretation of these values should be left to the opMode using it (such as scaling a value to get a motor power)
 *
 * Note: for motors/servos, you could use the resourceID for that device (see devices.xml)
 */
public abstract class Controls {
    public static final int ID_MOTOR_LEFT_WHEEL = R.string.motor_wheel_left;
    public static final int ID_MOTOR_RIGHT_WHEEL = R.string.motor_wheel_right;

    protected Context mContext;
    protected Gamepad mGamepad1;
    protected Gamepad mGamepad2;

    protected Resources mResources;

    /**
     * Create a new Controls using the gamepads and context assocaited with the given opMode
     * @param opMode opMode to grab context and Gamepads from
     */
    public Controls(OpMode opMode) {
        mGamepad1 = opMode.gamepad1;
        mGamepad2 = opMode.gamepad2;
        mContext = opMode.hardwareMap.appContext;
        mResources = mContext.getResources();
    }

    /**
     * Returns the controller value for the given id
     * @param id id to get the value for
     * @return resulting value
     */
    public float getValue(int id) {
        return getValue(id, 0);
    }

    /**
     * Returns the controller value for the given id
     * @param id id to get the value for
     * @param defaultValue value to use if we could not find a controller defined value
     * @return resulting value
     */
    public float getValue(int id, float defaultValue) {
        switch(id) {
            case(ID_MOTOR_LEFT_WHEEL):
                return getLeftWheelValue();
            case(ID_MOTOR_RIGHT_WHEEL):
                return getRightWheelValue();
            default:
                return defaultValue;
        }
    }

    /**
     * Returns the controller value associated with the left wheel motor
     * @return a float between [-1,1]
     */
    public abstract float getLeftWheelValue();

    /**
     * Returns the controller value associated with the right wheel motor
     * @return a float between [-1,1]
     */
    public abstract float getRightWheelValue();

    /**
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    public static float scaleInput(float dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return (float) dScale;
    }
}

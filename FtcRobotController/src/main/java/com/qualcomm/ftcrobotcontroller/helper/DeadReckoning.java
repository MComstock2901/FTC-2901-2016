package com.qualcomm.ftcrobotcontroller.helper;

import android.drm.DrmStore;

/**
 * Created by michaelcomstock on 11/21/15.
 */
public class DeadReckoning {
    private static float xPosition, yPosition, thetaPosition = 0;
    private int leftEncoderCount, rightEncoderCount;
    private float motorCountToRotation;
    private float motorRotationToDistance;
    private float wheelbase, wheelDiameter;

    public DeadReckoning () {

    }

    public float[] getPosition (int leftEncoderCount, int rightEncoderCount) {
        float[] returnPosition;

        //identifying the change that has occured in the encoder count
        int changeLeftEncoder, changeRightEncoder;
        changeLeftEncoder = leftEncoderCount - this.leftEncoderCount;
        changeRightEncoder = rightEncoderCount - this.rightEncoderCount;
        //setting the old encoder counts to the new iteration
        this.leftEncoderCount = leftEncoderCount;
        this.rightEncoderCount = rightEncoderCount;

        float leftMotorRotation = getRotations(changeLeftEncoder);
        float rightMotorRotation = getRotations(changeRightEncoder);

        float leftDistanceTravelled = getDistance(leftMotorRotation);
        float rightDistanceTravelled = getDistance(rightMotorRotation);

        //WARNING!!!! THIS IS NOT SUPPOSED TO RETURN NULL!!!!!
        return null;
    }

    private float getRotations (int encoderCount){
        float motorRotations;
        motorRotations = encoderCount/motorCountToRotation;
        return motorRotations;
    }

    private float getDistance (float motorRotation){
        float distanceTravelled;
        float wheelCircumference = (float)(wheelDiameter * Math.PI);
        distanceTravelled = motorRotation * wheelCircumference;
        return distanceTravelled;
    }

    private float getLinearSpeed (float leftWheelDistance, float rightWheelDistance){
        float linearSpeed;
        linearSpeed = (leftWheelDistance + rightWheelDistance) / 2;
        return linearSpeed;
    }

    private float getAngularSpeed (float leftWheelDistance, float rightWheelDistance, float wheelbase){
        float angularSpeed;
        angularSpeed = (rightWheelDistance - leftWheelDistance) / wheelbase;
        return angularSpeed;
    }
}

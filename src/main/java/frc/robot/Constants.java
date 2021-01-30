// BlitzCreek 3770 - Genesis Project
// Constants File
// Many of the constants in one file.

package frc.robot;

public final class Constants
{
    public static final int LEFT_STICK_USB_PORT  = 0;
    public static final int RIGHT_STICK_USB_PORT = 1;
    public static final int CONTROLLER_USB_PORT  = 2;

    // CAN ID's for DeltaBot2
    // Left1: 2
    // Left2: 3
    // Right1: 1
    // Right2: 4
        
    // Motor CAN ID's (OLLE)
    public static final int RIGHT_MOTOR1_CAN_ID            = 4;
    public static final int RIGHT_MOTOR2_CAN_ID            = 5;
    public static final int LEFT_MOTOR1_CAN_ID             = 6;
    public static final int LEFT_MOTOR2_CAN_ID             = 7;

    public static final int INTAKE_SIDE_MOTOR_CAN_ID        = 11;
    public static final int INTAKE_FRONTBACK_MOTOR_CAN_ID   = 12;

    public static final int MOVING_MOTOR_CAN_ID             = 15;
    public static final int LOADING_MOTOR_CAN_ID            = 16;
    public static final int SHOOTER_MOTOR_CAN_ID            = 10;

    public static final double GYRO_PID_P = 0.01;
    public static final double GYRO_PID_I = 0.0;
    public static final double GYRO_PID_D = 0.0;
    public static final double GYRO_PID_TOLERANCE = 2.0;
}

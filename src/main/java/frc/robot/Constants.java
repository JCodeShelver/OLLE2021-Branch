// BlitzCreek 3770 - OLLE 2021
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

    public static final int SHOOTER_MOTOR_CAN_ID            = 10;
    public static final int INTAKE_SIDE_MOTOR_CAN_ID        = 11;
    public static final int INTAKE_FRONTBACK_MOTOR_CAN_ID   = 12;

    public static final int MOVING_MOTOR_CAN_ID             = 15;
    public static final int LOADING_MOTOR_CAN_ID            = 16;

    public static final double GYRO_PID_P = 0.01;
    public static final double GYRO_PID_I = 0.0;
    public static final double GYRO_PID_D = 0.0;
    public static final double GYRO_PID_TOLERANCE = 2.0;

    // Autonomous Driving variables.
    public static final double ANGLE_TOLERANCE                   = 3.0;  // Stop turn if within this number of degrees
    public static final double DISTANCE_TOLERANCE                = 3.0;    // Absolute value of driving distance tolerance 
    public static final double RAMP_DOWN_DIST                    = 24.0;   // Distance (inches) to target to decelerate
    public static final double RAMP_UP_TIME                      = 1.0;    // Time to ramp up speed in auton
    public static final double TURN_TIME_LIMIT                   = 5.0;  // Stop turn after this time - as backup

    // Inches per Tick conversion for drive encoder.
    public static final double INCHES_PER_TICK                   = 0.052019;

    // Limelight's PID settings.
    public static final double VISION_X_PID_TOLERANCE            = 1.0;    // Absolute vision distance angle range
    public static final double VISION_PID_P                      = 0.03;
    public static final double VISION_PID_I                      = 0.06;
    public static final double VISION_PID_D                      = 0.0025;
    public static final double VISION_X_OFFSET                   = -30.0;


    // Shooter mechanism's PID settings.
    // PID_P = .0025, PID_I = 0.01, PID_D = 0.000175;
    public static final double SHOOTER_PID_P                     = 0.001;
    public static final double SHOOTER_PID_I                     = 0.01;
    public static final double SHOOTER_PID_D                     = 0.000175;
    public static final double SHOOTER_TICKS_PER_RPM             = 6.837;

    // PCM 0 Ports
    public static final int SHOOTER_FIRE_CYLINDER_INPORT         = 0;
    public static final int SHOOTER_FIRE_CYLINDER_OUTPORT        = 1;

    public static final int INTAKE_CYLINDER_OUTPORT              = 2;
    public static final int INTAKE_CYLINDER_INPORT               = 3;

    // --------------------------------------------------------------------------
    
    public static boolean ballInShooter;
    public static boolean EndgameEnabled;
    public static boolean shooterSystemActive;
}

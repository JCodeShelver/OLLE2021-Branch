// BlitzCreek 3770 - OLLE 2021
// DriveSystem Subsystem
// Controlls the drivetrain

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

// Import Constants
import frc.robot.Constants;

public class DriveSystem extends SubsystemBase
{
  // Set vars
  private final CANSparkMax leftMotor1,  leftMotor2;
  private final CANSparkMax rightMotor1, rightMotor2;
  private final TalonSRX    encoderReading;

  private       boolean     linearOn, fullSpeed; // Determines driving input mode.

  private       double      adjustedLeft, adjustedRight;
  
  // ----------------------------------------------------------------------------
  // Constructor
  public DriveSystem()
  {
    leftMotor1     = new CANSparkMax(Constants.LEFT_MOTOR1_CAN_ID, MotorType.kBrushless);
    leftMotor2     = new CANSparkMax(Constants.LEFT_MOTOR2_CAN_ID, MotorType.kBrushless);
    rightMotor1    = new CANSparkMax(Constants.RIGHT_MOTOR1_CAN_ID, MotorType.kBrushless);
    rightMotor2    = new CANSparkMax(Constants.RIGHT_MOTOR2_CAN_ID, MotorType.kBrushless);
    
    encoderReading = new TalonSRX(Constants.MOVING_MOTOR_CAN_ID);
    this.zeroEncoder();

    leftMotor1.setIdleMode(IdleMode.kBrake);
    leftMotor2.setIdleMode(IdleMode.kBrake);
    rightMotor1.setIdleMode(IdleMode.kBrake);
    rightMotor2.setIdleMode(IdleMode.kBrake);
    
    linearOn  = false;
    fullSpeed = true;

    leftMotor1.setInverted(false);
    leftMotor2.setInverted(false);
    rightMotor1.setInverted(true);
    rightMotor2.setInverted(true);
  }

  // ----------------------------------------------------------------------------
  // Toggles between linear scaling and quadratic scaling.
  public void toggleScale()
  {
    linearOn = !linearOn;
  }

  // ----------------------------------------------------------------------------
  // Toggles between full range of speed and only half range.
  public void toggleSpeed()
  {
    fullSpeed = !fullSpeed;
  }

  // ----------------------------------------------------------------------------
  // Manipulates the input based on speed and scaling vars, then returns an array.
  public double[] manipInput(double left, double right)
  {
    if (Math.abs(left) < 0.1)
      left = 0.0;
  
    if (Math.abs(right) < 0.1)
      right = 0.0;
    
    if (linearOn)
    {  
      adjustedLeft  = left;
      adjustedRight = right;
    } else {
      adjustedLeft  = left  * Math.abs(left);
      adjustedRight = right * Math.abs(right);
    }

    if (!fullSpeed)
    {
      adjustedLeft  /= 2.0;
      adjustedRight /= 2.0;
    }

    double[] adjustedLR = {adjustedLeft, adjustedRight};
    return adjustedLR;
  }

  // ----------------------------------------------------------------------------
  // Drives the motors based on HUMAN input.
  public void drive(double left, double right)
  {
    // SmartDashboard.putNumber("LeftRaw", left);
    // SmartDashboard.putNumber("RightRaw", right);    
    double[] adjustedInputs = manipInput(left, right);
    
    leftMotor1.set(adjustedInputs[0]);
    leftMotor2.set(adjustedInputs[0]);
    rightMotor1.set(adjustedInputs[1]);
    rightMotor2.set(adjustedInputs[1]);
  }
  
  // ----------------------------------------------------------------------------
  // Raw drive method, used by Autonomous commands so as to not mess with the
  // scale.
  public void rdrive(double left, double right)
  {
    leftMotor1.set(left);
    leftMotor2.set(left);
    rightMotor1.set(right);
    rightMotor2.set(right);
  }
  
  // ----------------------------------------------------------------------------
  // Sets each motor to 0, stopping the robot.
  public void kill()
  {
    leftMotor1.set(0);
    leftMotor2.set(0);
    rightMotor1.set(0);
    rightMotor2.set(0);
  }
  
  // ----------------------------------------------------------------------------
  // Returns the distance traveled in inches.
  public double getDistanceInches()
  {
    return Math.abs(encoderReading.getSelectedSensorPosition() * Constants.INCHES_PER_TICK);
  }

  // ----------------------------------------------------------------------------
  // Zeroes the encoder.
  public void zeroEncoder()
  {
    encoderReading.setSelectedSensorPosition(0);
  }
}

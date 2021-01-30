// BlitzCreek 3770 - Genesis Project
// DriveSystem Subsystem
// Controlls the drivetrain

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Counter;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

public class DriveSystem extends SubsystemBase
{
  private CANSparkMax leftMotor1, leftMotor2;
  private CANSparkMax rightMotor1, rightMotor2;
  private Counter leftEncoder, rightEncoder;

  private boolean linearOn; // Determines driving input mode.

  private double adjustedLeft, adjustedRight;
  
  public DriveSystem()
  {
    leftMotor1  = new CANSparkMax(Constants.LEFT_MOTOR1_CAN_ID, MotorType.kBrushless);
    leftMotor2  = new CANSparkMax(Constants.LEFT_MOTOR2_CAN_ID, MotorType.kBrushless);
    rightMotor1 = new CANSparkMax(Constants.RIGHT_MOTOR1_CAN_ID, MotorType.kBrushless);
    rightMotor2 = new CANSparkMax(Constants.RIGHT_MOTOR2_CAN_ID, MotorType.kBrushless);

    linearOn = true;

    leftMotor1.setInverted(true);
    leftMotor2.setInverted(true);
    rightMotor1.setInverted(false);
    rightMotor2.setInverted(false);
  }

  public void toggleMode()
  {
    linearOn = !linearOn;
  }

  public void drive(double left, double right)
  {
    if (linearOn)
    {
      adjustedLeft = left;
      adjustedRight = right;
    } else {
      if (left < 0)
        adjustedLeft = -left * left;
      else
        adjustedLeft = left * left;
        
      if (right < 0)
        adjustedRight = -right * right;
      else
        adjustedRight = right * right;
    }

    leftMotor1.set(adjustedLeft);
    leftMotor2.set(adjustedLeft);
    rightMotor1.set(adjustedRight);
    rightMotor2.set(adjustedRight);
  }

  public void kill()
  {
    leftMotor1.set(0);
    leftMotor2.set(0);
    rightMotor1.set(0);
    rightMotor2.set(0);
  }

  public void zeroEncoder()
  {
    leftEncoder.reset();
    rightEncoder.reset();
  }
}

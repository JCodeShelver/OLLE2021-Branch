// BlitzCreek 3770 - OLLE 2021
// DriveSystem Subsystem
// Controlls the drivetrain

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Counter;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveSystem extends SubsystemBase
{
  private CANSparkMax leftMotor1, leftMotor2;
  private CANSparkMax rightMotor1, rightMotor2;
  private Counter leftEncoder, rightEncoder;

  private boolean linearOn, fullSpeed = true; // Determines driving input mode.

  private double adjustedLeft, adjustedRight;
  
  public DriveSystem()
  {
    leftMotor1  = new CANSparkMax(Constants.LEFT_MOTOR1_CAN_ID, MotorType.kBrushless);
    leftMotor2  = new CANSparkMax(Constants.LEFT_MOTOR2_CAN_ID, MotorType.kBrushless);
    rightMotor1 = new CANSparkMax(Constants.RIGHT_MOTOR1_CAN_ID, MotorType.kBrushless);
    rightMotor2 = new CANSparkMax(Constants.RIGHT_MOTOR2_CAN_ID, MotorType.kBrushless);

    leftMotor1.setInverted(true);
    leftMotor2.setInverted(true);
    rightMotor1.setInverted(false);
    rightMotor2.setInverted(false);
  }

  public void toggleMode()
  {
    linearOn = !linearOn;
  }

  public void toggleSpeed()
  {
    fullSpeed = !fullSpeed;
  }

  public double[] manipInput(double left, double right)
  {
    if (linearOn)
    {
      if (!(Math.abs(left) < 0.1))
      left = 0.0;
    
      if (!(Math.abs(right) < 0.1))
        right = 0.0;
      
      adjustedLeft = left;
      adjustedRight = right;
    } else {
        adjustedLeft = left * Math.abs(left);
        adjustedRight = right * Math.abs(right);
    }

    if (!fullSpeed)
    {
      adjustedLeft /= 2.0;
      adjustedRight /= 2.0;
    }

    double[] adjustedLR = {adjustedLeft, adjustedRight};
    return adjustedLR;
  }

  public void drive(double left, double right)
  {
    double[] adjustedInputs = manipInput(left, right);
    
    leftMotor1.set(adjustedInputs[0]);
    leftMotor2.set(adjustedInputs[0]);
    rightMotor1.set(adjustedInputs[1]);
    rightMotor2.set(adjustedInputs[1]);
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

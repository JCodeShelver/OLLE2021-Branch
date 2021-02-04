// FRC Team 3770 - BlitzCreek - OLLE 2021
// Shooter Subsystem
// Controls the ball shooter system, and 
// utilizes PID controller for motor speed.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Import External Libraries
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

// Import Constants
import frc.robot.Constants;

public class Shooter extends SubsystemBase 
{
  // Set vars
  private final DoubleSolenoid ShootingPiston;
  private final DigitalInput   BallInShooter;
  private final PIDController  ShooterPID;
  private final TalonSRX       shooterMotor;
  
  private boolean              shooterPistonDown;
  
  private double               currentSetPoint, TPM;

  // ----------------------------------------------------------------------------
  // Constructor - (Do nothing)
  public Shooter() 
  {
    ShootingPiston = new DoubleSolenoid(0, Constants.SHOOTER_FIRE_CYLINDER_INPORT, Constants.SHOOTER_FIRE_CYLINDER_OUTPORT);
    BallInShooter  = new DigitalInput(3);
    shooterMotor   = new TalonSRX(Constants.SHOOTER_MOTOR_CAN_ID);
    ShooterPID     = new PIDController(Constants.SHOOTER_PID_P, Constants.SHOOTER_PID_I, Constants.SHOOTER_PID_D);
    TPM            = 0;

    shooterPistonDown = true;
  }

  // ----------------------------------------------------------------------------
  // Get the RPM of the motor of the Shooter mechanism.
  public double getRPM()
  {
    return shooterMotor.getSelectedSensorVelocity() / Constants.SHOOTER_TICKS_PER_RPM;
  }
  
  // ----------------------------------------------------------------------------
  // Get the Set Point.
  public double getSetPoint()
  {
    return currentSetPoint;
  }
  
  // ----------------------------------------------------------------------------
  // Return the state of the Shooter mechanism pneumatic.
  public boolean isShooterPistonDown()
  {
    return shooterPistonDown;
  }
  
  // ----------------------------------------------------------------------------
  // Lower the Shooter mechanism pneumatic.
  public void lowerShootingPiston()
  {
    ShootingPiston.set(DoubleSolenoid.Value.kForward);
    shooterPistonDown = true;
  }
  
  // ----------------------------------------------------------------------------
  // Set the motor of the Shooter mechanism to full speed.
  public void motorOnFull()
  {
    shooterMotor.set(ControlMode.PercentOutput, -1.0);
  }
  
  // ----------------------------------------------------------------------------
  // Set the Set Point.
  public void setSetPoint(double s)
  {
    currentSetPoint = -s;
  }
  
  // ----------------------------------------------------------------------------
  // Raise the Shooter mechanism pneumatic.
  public void shootBall()
  {
    ShootingPiston.set(DoubleSolenoid.Value.kReverse);
    shooterPistonDown = false;
  }
  
  // ----------------------------------------------------------------------------
  // Spin motor up to current set point.  Designed to be periodically called.
  // Precondition:  SetPoint has been set!
  public void spinToSetPoint()
  {
    TPM = shooterMotor.getSelectedSensorVelocity();
    SmartDashboard.putNumber("Shooter RPM", TPM / Constants.SHOOTER_TICKS_PER_RPM);

    //currentSetPoint = -3700;
    System.out.println("Current Set point for RPM: " + currentSetPoint);
    System.out.println("Current RPM: " +   this.getRPM());

    double pidOutput = ShooterPID.calculate(TPM / Constants.SHOOTER_TICKS_PER_RPM, currentSetPoint);

    //System.out.println("Motor: " + pidOutput);
    shooterMotor.set(ControlMode.PercentOutput, pidOutput);
  }

  // ----------------------------------------------------------------------------
  // Turn the Shooter mechanism off.
  public void stop()
  {
    currentSetPoint = 0.0;
    shooterMotor.set(ControlMode.PercentOutput, 0.0);
    this.lowerShootingPiston();
  } 

  // ----------------------------------------------------------------------------
  // Update state of variables concerning balls in the Shooter mechanism.
  public void updateBallInShooter()
  {
    Constants.ballInShooter = !BallInShooter.get();
    SmartDashboard.putBoolean("Ball In Shooter", Constants.ballInShooter);
  }
}

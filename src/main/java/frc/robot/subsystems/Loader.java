// FRC Team 3770 - BlitzCreek - OLLE 2021
// Loader Subsystem
// Controlls the conveyor mechanism and
// ball detection sensors.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

// Import External Libraries
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;  

// Import Constants
import frc.robot.Constants;

public class Loader extends SubsystemBase 
{ 
  // Set vars
  private final DigitalInput backSwitch, fullSwitch, intakeSwitch;
  private final TalonSRX loadingMotor, queueingMotor;

  // ----------------------------------------------------------------------------
  // Constructor
  public Loader() 
  {
    loadingMotor   = new TalonSRX(Constants.LOADING_MOTOR_CAN_ID);
    queueingMotor  = new TalonSRX(Constants.MOVING_MOTOR_CAN_ID); 
    
    backSwitch     = new DigitalInput(2);
    fullSwitch     = new DigitalInput(1);
    intakeSwitch   = new DigitalInput(0);   
  }

  // ----------------------------------------------------------------------------
  // Returns the state of the back sensor.
  public boolean ballWaiting()
  {
    return !backSwitch.get();
  }

  // ----------------------------------------------------------------------------
  // Returns the state of the intake sensor.
  public boolean ballAtIntake()
  {
    return !intakeSwitch.get();
  }

  // ----------------------------------------------------------------------------
  // Returns the state of the full sensor.
  public boolean ballInSystem()
  {
    return !fullSwitch.get();
  }

  // ----------------------------------------------------------------------------
  // Turn the loading motor of the Loader mechanism off.
  public void LoadBallMotorOff()
  {
    loadingMotor.set(ControlMode.PercentOutput, 0.0);
  }

  // ----------------------------------------------------------------------------
  // Turn the loading motor  of the Loader mechanism on.
  public void LoadBallMotorOn()
  {
    loadingMotor.set(ControlMode.PercentOutput, 1.0);
  }  

  // ----------------------------------------------------------------------------
  // Turn the moving motor of the Loader mechanism off.
  public void QueueMotorOff() 
  {
    queueingMotor.set(ControlMode.PercentOutput, 0.0); 
  }  

  // ----------------------------------------------------------------------------
  // Turn the moving motor of the Loader mechanism on.
  public void QueueMotorOn(double in) 
  {
    queueingMotor.set(ControlMode.PercentOutput, in); 
  }  
}
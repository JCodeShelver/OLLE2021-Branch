// FRC Team 3770 - BlitzCreek - OLLE 2021
// Front Intake Subsystem
// Manage the ball intake mechanism.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;

// Import External Libraries
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;  

// Import Constants
import frc.robot.Constants;

public class FrontIntake extends SubsystemBase 
{ 
  // Set vars
  private final DoubleSolenoid deployCylinder;
  private final TalonSRX intakeMotor, sideToSideMotor;
 
  // Indicates if the Intake is deployed.
  private boolean isOut = false;

  // ----------------------------------------------------------------------------
  // Constructor
  public FrontIntake() 
  {
    deployCylinder  = new DoubleSolenoid(0, Constants.INTAKE_CYLINDER_INPORT, Constants.INTAKE_CYLINDER_OUTPORT);
    
    intakeMotor     = new TalonSRX(Constants.INTAKE_FRONTBACK_MOTOR_CAN_ID); 
    sideToSideMotor = new TalonSRX(Constants.INTAKE_SIDE_MOTOR_CAN_ID);
    
  }
  
  // ----------------------------------------------------------------------------
  // Manage the Intake mechanism motors.
  public void driveIntakeMotors(double input) 
  {
    intakeMotor.set(ControlMode.PercentOutput, input); 
    sideToSideMotor.set(ControlMode.PercentOutput, input);
  }
  
  // ----------------------------------------------------------------------------
  // Returns the state of the pneumatics for the Front part of the Intake mechanism.
  public boolean isOut()
  {
    return isOut;
  }
  
  // ----------------------------------------------------------------------------
  // Stows the Front part of the Intake mechanism.
  public void pullUp() 
  {
    deployCylinder.set(DoubleSolenoid.Value.kReverse);
    isOut = false;
  }

  // ----------------------------------------------------------------------------
  // Deploys the Front part of the Intake mechanism. 
  public void pushDown() 
  {
    deployCylinder.set(DoubleSolenoid.Value.kForward);
    isOut = true;
  }
}
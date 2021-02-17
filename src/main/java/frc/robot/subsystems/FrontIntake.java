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
  private final TalonSRX intakeMotor, STSMotor;
 
  // Indicates if the Intake is deployed.
  private boolean isOut, isDisabled = false;

  // ----------------------------------------------------------------------------
  // Constructor
  public FrontIntake() 
  {
    deployCylinder  = new DoubleSolenoid(0, Constants.INTAKE_CYLINDER_INPORT, Constants.INTAKE_CYLINDER_OUTPORT);
    
    intakeMotor     = new TalonSRX(Constants.INTAKE_FRONTBACK_MOTOR_CAN_ID); 
    STSMotor        = new TalonSRX(Constants.INTAKE_SIDE_MOTOR_CAN_ID);
  }

  // ----------------------------------------------------------------------------
  // Manage the Intake mechanism motors.
  public void drive(double input) 
  {
    intakeMotor.set(ControlMode.PercentOutput, input); 
    STSMotor.set(ControlMode.PercentOutput, input);
  }

  // ----------------------------------------------------------------------------
  // Stops the intake motors.
  public void stop()
  {
    intakeMotor.set(ControlMode.PercentOutput, 0);
    STSMotor.set(ControlMode.PercentOutput, 0);
    
    isDisabled = !isDisabled;
  }

  // ----------------------------------------------------------------------------
  // Returns the state of the pneumatics for the Front part of the Intake mechanism.
  public boolean isOut()
  {
    return isOut;
  }
  
  // ----------------------------------------------------------------------------
  // Returns if the Front Intake motors have been forcibly stopped.
  public boolean isDisabled()
  {
    return isDisabled;
  }
  
  // ----------------------------------------------------------------------------
  // Either stows or deploys the Front part of the Intake mechanism.
  public void move() 
  {
    //SmartDashboard.putBoolean("Intake Pneumatic", deployCylinder.get() == DoubleSolenoid.Value.kReverse);
    intakeMotor.set(ControlMode.PercentOutput, 0);
    STSMotor.set(ControlMode.PercentOutput, 0);

    if (deployCylinder.get() == DoubleSolenoid.Value.kForward)
      deployCylinder.set(DoubleSolenoid.Value.kReverse);
    else
      deployCylinder.set(DoubleSolenoid.Value.kForward);
    
    isOut = !isOut;
  }
}
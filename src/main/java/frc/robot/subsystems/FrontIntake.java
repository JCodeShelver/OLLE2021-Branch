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
    deployCylinder.set(DoubleSolenoid.Value.kForward);
    
    intakeMotor     = new TalonSRX(Constants.INTAKE_FRONTBACK_MOTOR_CAN_ID); 
    STSMotor        = new TalonSRX(Constants.INTAKE_SIDE_MOTOR_CAN_ID);
  }
  
  // ----------------------------------------------------------------------------
  // Manage the Intake mechanism motors.
  public void driveIntakeMotors(double input) 
  {
    if (!isDisabled)
    {
      intakeMotor.set(ControlMode.PercentOutput, input); 
      STSMotor.set(ControlMode.PercentOutput, input);
    } else {
      intakeMotor.set(ControlMode.PercentOutput, 0);
      STSMotor.set(ControlMode.PercentOutput, 0);
    }
  }

  // ----------------------------------------------------------------------------
  // Stops the intake motors.
  public void stop()
  {
    intakeMotor.set(ControlMode.PercentOutput, 0);
    STSMotor.set(ControlMode.PercentOutput, 0);

    isDisabled = !isDisabled;
  }

  // Fix isOut
  public void debug()
  {
    isOut = !isOut;
  }
  
  // ----------------------------------------------------------------------------
  // Returns the state of the pneumatics for the Front part of the Intake mechanism.
  public boolean isOut()
  {
    return isOut;
  }
  
  // ----------------------------------------------------------------------------
  // Either stows or deploys the Front part of the Intake mechanism.
  public void move() 
  {
    deployCylinder.toggle();
    isOut = !isOut;
  }
}
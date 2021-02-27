// BlitzCreek 3770 - OLLE 2021
// DriveTurn Autonomous Command
// Command that will control DriveSystem subsystem and use
// encoder measure pivot turn to a desired angle measure.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.GyroPID;

public class DriveTurn extends CommandBase
{
  // Class member objects
  private final DriveSystem driveSystem;
  private final GyroPID     gyroPID;
  
  private       Timer       segmentDriveTimer;
  
  // Required work variables
  private       double      angleRotateMotorAdjust, targetAngle, left, right;      // For adjusting left/right motors for angle correction

  // ----------------------------------------------------------------------------
  // Constructor:  Capture time and motor level for straight drive
  public DriveTurn(DriveSystem d, GyroPID g, double angle) 
  {
    driveSystem       = d;
    gyroPID           = g;
    
    targetAngle       = angle;

    segmentDriveTimer = new Timer();
    
    addRequirements(driveSystem);   
  }

  // ----------------------------------------------------------------------------
  // Called just before this Command runs the first time
  @Override
  public void initialize() 
  {
    // Start clock for this action
    segmentDriveTimer.reset(); 
    segmentDriveTimer.start();
    
    // Capture key parameters of turn
    gyroPID.enable();
    gyroPID.setSetpoint(targetAngle);
    gyroPID.setPvalue(0.005); // Replace with Constants.GYRO_P_VALUE
    //gyroPID.setIvalue(0.001); 
  }
  
  // ----------------------------------------------------------------------------
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute()
  {
    // Run repeatedly when not within the angle tolerance
    if (Math.abs(gyroPID.getMeasurement() - targetAngle) > Constants.ANGLE_TOLERANCE)
    {
      // Get motor adjust PID output from gyro
      angleRotateMotorAdjust = gyroPID.getOutput();
      System.out.println("GYROPID OUTPUT: " + angleRotateMotorAdjust);

      // Adjust left/right motor sets to PID output.  Rotate
      // as needed toward target angle
      left  = (+angleRotateMotorAdjust);
      right = (-angleRotateMotorAdjust);
      System.out.println("LeftTurnValue: " + left);
      System.out.println("RightTurnValue: " + right);

      driveSystem.rdrive(left, right);
    }
  }
  
  // ----------------------------------------------------------------------------
  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() 
  {
    // If not get at target angle (within tolerance) AND time limit not
    // reached, continue to update drive system motors.  Otherwise, stop.
    return (Math.abs(gyroPID.getMeasurement() - targetAngle) <= Constants.ANGLE_TOLERANCE);
  }

  // ----------------------------------------------------------------------------
  // Ending cleanup.
  @Override
  public void end(boolean interrupted)
  {
    driveSystem.kill();
    gyroPID.disable();
  }
}

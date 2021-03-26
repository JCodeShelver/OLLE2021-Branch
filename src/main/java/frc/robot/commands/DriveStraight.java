// BlitzCreek 3770 - OLLE 2021
// Drive Straight Autonomous Command
// Command that will control DriveSystem subsystem and use
// encoder measure to drive a given distance in inches.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.GyroPID;

public class DriveStraight extends CommandBase 
{
  // Class member objects
  private final DriveSystem driveSystem;
  private final GyroPID     gyroPID;

  private       Timer       segmentDriveTimer;
  
  // Required work variables
  private       double      angleMotorAdjust, left, right, percentage, powerLevel, targetAngle, targetDistance;

  // ----------------------------------------------------------------------------
  // Constructor:  Capture time and motor level for straight drive
  public DriveStraight(DriveSystem d, GyroPID g, double power, double distance, double angle) 
  {
    // Ready required subsystems
    driveSystem       = d;
    gyroPID           = g;

    // Capture key parameters of drive segment
    powerLevel        = power;
    targetAngle       = angle;
    targetDistance    = distance;   
    
    // Instantiate emergency stop timer
    segmentDriveTimer = new Timer();
    
    addRequirements(driveSystem);
  }

  // ----------------------------------------------------------------------------
  // Called just before this Command runs the first time
  @Override
  public void initialize() 
  {
    // Start emergency stop timer for this action
    segmentDriveTimer.reset(); 
    segmentDriveTimer.start();
    
    // Initialize gyro and encoder sensors
    gyroPID.enable();

    // So if we input 360°, then just go straight relative to current angle.
    if (targetAngle == 360)
      gyroPID.setSetpoint(gyroPID.getMeasurement());
    else
      gyroPID.setSetpoint(targetAngle);
    
    gyroPID.setPvalue(0.01);
    
    driveSystem.zeroEncoder();
  }
  
  // ----------------------------------------------------------------------------
  // 
  @Override
  public void execute()
  {
    // Periodic action that runs while distance to targe exceeds tolerance.
    if (Math.abs(targetDistance - driveSystem.getDistanceInches()) > Constants.DISTANCE_TOLERANCE) 
    {
      // Get motor adjustment derived from gyro. This keeps us driving in straight line.
      angleMotorAdjust = gyroPID.getOutput();
      
      left             = powerLevel + angleMotorAdjust;
      right            = powerLevel - angleMotorAdjust;
      
      // If just starting segment, ramp up gradually (linearly) over time period
      // If within given distance to target, begin ramping speed down linearly.
      if (segmentDriveTimer.get() < Constants.RAMP_UP_TIME)               // Handle gradual ramp down
      {
        percentage = segmentDriveTimer.get() / Constants.RAMP_UP_TIME;
        left      *= percentage;
        right     *= percentage;
      }   
      else if (targetDistance - driveSystem.getDistanceInches() <= Constants.RAMP_DOWN_DIST)  // Handle gradual ramp down
      {
        percentage = (targetDistance - driveSystem.getDistanceInches()) / Constants.RAMP_DOWN_DIST;
        left      *= percentage;
        right     *= percentage;
      }

      driveSystem.rdrive(left, right);
    }
  }
  
  // ----------------------------------------------------------------------------
  // 
  @Override
  public boolean isFinished() 
  {
    // Continue driving until encoder measure OR emergency timer terminates action
    return (Math.abs(targetDistance - driveSystem.getDistanceInches()) <= Constants.DISTANCE_TOLERANCE || segmentDriveTimer.get() >= Constants.ANGLE_TOLERANCE);
  }

  // ----------------------------------------------------------------------------
  // 
  @Override
  public void end(boolean interrupted)
  {
    driveSystem.kill();
    driveSystem.zeroEncoder();
    gyroPID.disable();
  }
}


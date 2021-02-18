// FRC Team 3770 - BlitzCreek - OLLE 2021
// Prepare to Shoot command
// Prepares shooter motor for shooting.
// Motor speed set using vision feedback.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Import Subsystems
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.VisionPID;

// Import Constants
import frc.robot.Constants;

public class StartTheLauncher extends CommandBase {
  // Set vars
  private final Shooter shooterSystem;
  private final VisionPID visionPID;

  public Joystick leftStick, rightStick;

  public boolean ballInPlace, RPMGood, shooterPistonUp, XGood;
  public double Distance, RPM;

  // ----------------------------------------------------------------------------
  // Constructor: Capture time and motor level for straight drive
  public StartTheLauncher(Shooter s, VisionPID v)
  {
    // Capture references to existing robot subsystems.  Define them as requirements.
    shooterSystem   = s;
    visionPID       = v;
    
    addRequirements(shooterSystem);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  public void initialize() 
  {
    visionPID.enable();

    RPMGood    = false;
    XGood      = false;
    
    Distance   = 0;
    RPM        = 0;

    leftStick = new Joystick(Constants.LEFT_STICK_USB_PORT);
    rightStick = new Joystick(Constants.RIGHT_STICK_USB_PORT);
    
    shooterSystem.updateBallInShooter();
  }
  
  // --------------------------------------------------------------------------
  // 
  public void execute() 
  {
    if (Constants.ballInShooter) // Check if the ball is in the shooter when we start!
    {
      Distance = yToDistanceFormula(visionPID.getYValue());
      SmartDashboard.putNumber("Distance from Target", Distance);

      // RPM = distanceToRPMFormula(Distance);
    
      if (rightStick.getRawButton(2))
        RPM = 1000; 
      else
        RPM = 3700; // Old value of 3700

      // RPM += leftStick.getRawAxis(3) * 200;

      shooterSystem.setSetPoint(RPM);
      shooterSystem.spinToSetPoint();

      visionPID.LEDon();  // I'm blinded by the lights.
      Constants.shooterSystemActive = true;
      shooterSystem.updateBallInShooter();
      visionPID.getVisionData();
      
      shooterSystem.setCanShoot((Math.abs(visionPID.getOutput()) <= 0.05), (Math.abs(shooterSystem.getSetPoint() - shooterSystem.getRPM()) <= 100));
      SmartDashboard.putBoolean("Can Shoot", shooterSystem.getCanShoot());
    }
  }
  
  // --------------------------------------------------------------------------
  // 
  public boolean isFinished() 
  {
    if (Constants.ballsControlled <= 0 && !Constants.ballInShooter)
    {
      shooterSystem.stop();
      return true;
    }

    if (shooterSystem.isDisabled())
    {
      shooterSystem.mstop();
      return true;
    }
    
    return false;
  }
  
  // --------------------------------------------------------------------------
  // Convert distance away to RPM
  private double distanceToRPMFormula(double d)
  {
    return 3700;
  }
  
  // --------------------------------------------------------------------------
  // Convert Limelight's Y to distance away
  private double yToDistanceFormula(double y)
  {
    // So Max found this via placing the robot a known distance away from the target,
    // recording the y angle, repeating a couple times and finding the curve of best fit.
    // Bar in robo Room
    // return 128 - 5.96 * (y) + 0.172 * (y * y);

    //Actual target on test frame
    return 90.2 - 1.33 * y + 0.213 * y * y;
  }

  // --------------------------------------------------------------------------
  //
  @Override 
  public void end(boolean interrupted)      
  { 
    shooterSystem.stop();
    Constants.shooterSystemActive = false;
    visionPID.LEDoff();
  }

  // --------------------------------------------------------------------------
  //
  protected void interrupted() 
  {
    shooterSystem.stop();
  }
}
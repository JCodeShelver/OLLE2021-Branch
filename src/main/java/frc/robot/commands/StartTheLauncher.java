// FRC Team 3770 - BlitzCreek - OLLE 2021
// Prepare to Shoot command
// Prepares shooter motor for shooting.
// Motor speed set using vision feedback.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.VisionPID;

public class StartTheLauncher extends CommandBase {
  // Set vars
  private final Shooter   shooterSystem;
  private final VisionPID visionPID;

  public        Joystick  leftStick, rightStick;

  public        boolean   ballInPlace, RPMGood, shooterPistonUp, XGood;
  public        double    Distance, RPM;

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

    leftStick  = new Joystick(Constants.LEFT_STICK_USB_PORT);
    rightStick = new Joystick(Constants.RIGHT_STICK_USB_PORT);
  }
  
  // --------------------------------------------------------------------------
  // 
  public void execute() 
  {
    Distance = yToDistanceFormula(visionPID.getYValue());
    SmartDashboard.putNumber("Distance from Target", Distance);

    // RPM = distanceToRPMFormula(Distance);
  
    if (rightStick.getRawButton(2))
      RPM = 1000; 
    else
      RPM = 3900; // Old value of 3700

    // RPM gets tweaked in increments of 200 RPM.
    RPM += Math.round(-leftStick.getRawAxis(3) / 0.1) * 0.1 * 200;

    shooterSystem.setSetPoint(RPM);
    shooterSystem.spinToSetPoint();

    visionPID.LEDon();  // I'm blinded by the lights.
    Constants.shooterSystemActive = true;
    shooterSystem.updateBallInShooter();
    visionPID.getVisionData();
    
    shooterSystem.setCanShoot((Math.abs(visionPID.getOutput()) <= 0.05), (Math.abs(shooterSystem.getSetPoint() - shooterSystem.getRPM()) <= 100));
    SmartDashboard.putBoolean("Can Shoot", shooterSystem.getCanShoot());

    /*
      After thinking about how to get the piston to stay up long enough to
      shoot, I've replicated last year's code so that when a button is
      pressed it shoots, otherwise it will lower the piston, and relies on 
      the fact that humans are slow.
    */
    if (leftStick.getRawButton(2))
      shooterSystem.shootBall();
    else
      shooterSystem.lowerShootingPiston();
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
  //
  @Override 
  public void end(boolean interrupted)      
  {
    if (interrupted)
      shooterSystem.stop();
     
    Constants.shooterSystemActive = false;
    visionPID.LEDoff();
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
    /* 
      So Max found this via placing the robot a known distance away from the target,
      recording the y angle, repeating a couple times and finding the curve of best
      fit.
    */

    // Bar in robo Room
    // return 128 - 5.96 * (y) + 0.172 * (y * y);

    // Actual target on test frame
    return 90.2 - 1.33 * y + 0.213 * y * y;
  }
}
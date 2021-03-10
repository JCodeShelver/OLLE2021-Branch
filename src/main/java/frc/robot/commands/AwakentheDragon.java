// BlitzCreek 3770 - OLLE 2021
// Front Intake Command
// Awakens the Dragon

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.FrontIntake;

public class AwakenTheDragon extends CommandBase 
{
  private final FrontIntake frontIntake;

  public AwakenTheDragon(FrontIntake f)
  {
    frontIntake = f;

    addRequirements(frontIntake);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  {
    // Make sure the front intake is down in Auton.
    if (Constants.currMode == Constants.Mode.AUTONOMOUS)
    {
      if (!frontIntake.isOut())
        frontIntake.move();
    }

    // Check that when we start this, we didn't just have Aerial spamming the A
    // button a lot.
    if (Constants.ballCaught && !Constants.ballAtIntake) 
      Constants.ballCaught = false;

    // If we didn't make it false before, make sure it's supposed to be true now.
    Constants.ballCaught = Constants.ballAtIntake;
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  @Override
  public void execute()
  {    
    if (frontIntake.isOut())
      frontIntake.drive(0.75);
    else
      frontIntake.drive(0.0);
  }

  // ----------------------------------------------------------------------------
  // Stop when a ball is picked up.
  @Override
  public boolean isFinished() 
  {
    if (!frontIntake.isOut())
    {
      frontIntake.stop();
      return true;
    }
    // Moved to entrance of conveyor (ball present and this is first time through)
    else if (Constants.ballAtIntake && !Constants.ballCaught)
    {
      Constants.ballsControlled ++;
      
      // Just to make sure we don't go past 3.
      if (Constants.ballsControlled >= 3)
        Constants.ballsControlled = 3;
      
      // Set our memory variable to true. "We caught a ball!"
      Constants.ballCaught = true;
      SmartDashboard.putBoolean("Ball Caught", Constants.ballCaught);
      
      return false;
    }
    // Picked up by conveyor and moved along.
    else if (!Constants.ballAtIntake && Constants.ballCaught)
    { 
      // Reset our memory variable. "We moved the ball!"
      Constants.ballCaught = false;
      SmartDashboard.putBoolean("Ball Caught", Constants.ballCaught);
      
      // Stop when we get 3 balls.
      return (Constants.ballsControlled >= 3);
    } else 
      return false;
  }

  // ----------------------------------------------------------------------------
  // Whenever we end this command, stop the motors.
  @Override
  public void end(boolean interrupted)
  {
    frontIntake.drive(0.0);

    if (Constants.currMode == Constants.Mode.AUTONOMOUS)
      frontIntake.move();
  }
}

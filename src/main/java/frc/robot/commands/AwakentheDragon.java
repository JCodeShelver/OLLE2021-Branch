// BlitzCreek 3770 - OLLE 2021
// Front Intake Command
// Awakens the Dragon

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.Loader;

public class AwakenTheDragon extends CommandBase 
{
  private final FrontIntake frontIntake;
  private final Loader      loader;

  public AwakenTheDragon(FrontIntake f, Loader l)
  {
    frontIntake = f;
    loader      = l;

    addRequirements(frontIntake, loader);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  {
    if (Constants.currMode == Constants.Mode.AUTONOMOUS)
    {
      if (!frontIntake.isOut())
        frontIntake.move();
    }
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  @Override
  public void execute()
  { 
    if (frontIntake.isOut() && !frontIntake.isDisabled() && !loader.ballAtIntake())
      frontIntake.drive(0.75);
    else
      frontIntake.drive(0.0);
  }

  // ----------------------------------------------------------------------------
  // Stop when a ball is picked up.
  @Override
  public boolean isFinished() 
  {
    if (!frontIntake.isOut() || frontIntake.isDisabled())
    {
      frontIntake.mstop();
      return true;
    }
    // Moved to entrance of conveyor
    else if (loader.ballAtIntake() && !Constants.ballCaught)
    {
      Constants.ballsControlled ++;
      
      if (Constants.currMode == Constants.Mode.TELEOP)
        return true;
      else {
        Constants.ballCaught = true;
        return false;
      }
    }
    // Picked up by conveyor
    else if (!loader.ballAtIntake() && Constants.ballCaught)
    {
      Constants.ballCaught = false;
      
      frontIntake.drive(0.0);
      
      return (Constants.ballsControlled == 3);
    } else 
      return false;
  }
}

// BlitzCreek 3770 - OLLE 2021
// Front Intake Command
// Awakens the Dragon

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.Loader;

public class AwakenTheDragon extends CommandBase 
{
  private final FrontIntake frontIntake;
  private final Loader      loader;
  

  public AwakenTheDragon(FrontIntake f, Loader l)
  {
    frontIntake = f;
    loader = l;

    addRequirements(frontIntake, loader);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  {

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
      frontIntake.stop();
      return true;
    }
      else if (loader.ballAtIntake())
    {
      Constants.ballsControlled ++;
      return true;
    }
    else
      return false;
  }
}

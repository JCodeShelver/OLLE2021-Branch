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
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  @Override
  public void execute()
  {    
    if (frontIntake.isOut() && !frontIntake.isDisabled())
      frontIntake.drive(0.75);
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
    else if (Constants.ballAtIntake && !Constants.ballCaught)
    {
      Constants.ballsControlled ++;
      
      if (Constants.ballsControlled >= 3)
        Constants.ballsControlled = 3;
      
      Constants.ballCaught = true;
      SmartDashboard.putBoolean("Ball Caught", Constants.ballCaught);

      return false;
    }
    // Picked up by conveyor
    else if (!Constants.ballAtIntake && Constants.ballCaught)
    {
      Constants.ballCaught = false;
      SmartDashboard.putBoolean("Ball Caught", Constants.ballCaught);
      
      return (Constants.ballsControlled >= 3);
    } else 
      return false;
  }

  @Override
  public void end(boolean interrupted)
  {
    frontIntake.drive(0.0);

    if (Constants.currMode == Constants.Mode.AUTONOMOUS)
      frontIntake.move();
  }
}

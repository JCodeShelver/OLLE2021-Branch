// BlitzCreek 3770 - OLLE 2021
// Queue Manager Command
// Compresses the Queue

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Loader;

public class QueueManager extends CommandBase 
{
  private final Loader loader;

  private       boolean ballInSystem, ballAtIntake, ballWaiting, isDone;
  
  public QueueManager(Loader l)
  {
    loader = l;

    addRequirements(loader);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  { 
    ballInSystem  = false;
    ballAtIntake  = false;
    ballWaiting   = false;
    isDone        = false;
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  //                                 |Shooter| ^
  //  |FrontIntake|InSystem|No Sensor|Waiting| |
  //   ------------------------------------>
  @Override
  public void execute()
  { 
    ballAtIntake = (loader.ballAtIntake()); // Ball just sucked up
    ballInSystem = (loader.ballInSystem()); // Ball within the middle of conveyor (may not use...)
    ballWaiting  = (loader.ballWaiting());  // Ball Waiting next to the shooter

    // Logic for Belt Movement

    // First, is there a ball that can be loaded into the shooter?
    if (ballWaiting && !Constants.ballInShooter)
    {
        loader.LoadBallMotorOn();
        loader.QueueMotorOn(0.5);
    }
    // If there isn't, but there is a ball waiting to be loaded, STOP THE REST OF THIS.
    else if (ballWaiting)
        loader.QueueMotorOff();
    // If the shooter is active, then start moving the queue.
    else if (Constants.shooterSystemActive)
        loader.QueueMotorOn(0.5);
    // If there is a ball just picked up, then move it.
    else if (ballAtIntake)
        loader.QueueMotorOn(0.5);
    // If there isn't any of the above, then turn off the queue motor.    
    else if (!ballAtIntake)
        isDone = true;
        loader.QueueMotorOff();
    
    // Once the ball reaches the shooter, shut off the STSmotor.
    if (Constants.ballInShooter)
        loader.LoadBallMotorOff();
  }

  // ----------------------------------------------------------------------------
  // Stop when a the conveyor has been compressed.
  @Override
  public boolean isFinished() 
  {
    return isDone;
  }
}

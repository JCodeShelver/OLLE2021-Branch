// BlitzCreek 3770 - OLLE 2021
// Queue Manager Command
// Compresses the Queue

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;

public class QueueManager extends CommandBase 
{
  private final Loader  loader;
  private final Shooter  shooter;

  private       boolean ballInSystem, ballAtIntake, ballWaiting, isDone;
  private       int     ballsInQueue;
  
  public QueueManager(Loader l, Shooter s)
  {
    loader  = l;
    shooter = s;

    addRequirements(loader, shooter);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  { 
    ballsInQueue  = 0;

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
    
    ballsInQueue = (Constants.ballInShooter) ? Constants.ballsInSystem - 1: Constants.ballsInSystem;
    
    ballAtIntake = (loader.ballAtIntake()); // Ball just sucked up
    ballInSystem = (loader.ballInSystem()); // Ball within the middle of conveyor (may not use...)
    ballWaiting  = (loader.ballWaiting());  // Ball Waiting next to the shooter
    
    shooter.updateBallInShooter();
    SmartDashboard.putString("DB/String 1", "Balls In Queue: " + ballsInQueue);
    SmartDashboard.putString("DB/String 2", "Balls In System: " + Constants.ballsInSystem);
    // Logic for Belt Movement

    // First, is there a ball that can be loaded into the shooter?
    if (ballWaiting && !Constants.ballInShooter)
    {
        loader.LoadBallMotorOn();
        loader.QueueMotorOn(0.5);
    }
    // If there isn't, but there is a ball waiting to be loaded, STOP THE REST OF THIS.
    else if (ballWaiting || ballsInQueue == 0)
    {
        loader.QueueMotorOff();
        isDone = true;
    }
    // If the shooter is active, then start moving the queue.
    else if (Constants.shooterSystemActive)
        loader.QueueMotorOn(0.5);
    // If there is a ball just picked up, then move it.
    else if (ballsInQueue > 1)
        loader.QueueMotorOn(0.5);
    
    // Once the ball reaches the shooter, shut off the STSmotor.
    if (Constants.ballInShooter)
        loader.LoadBallMotorOff();
  }

  // ----------------------------------------------------------------------------
  // Stop when a the conveyor has been compressed.
  @Override
  public boolean isFinished() 
  {
    return isDone || loader.plzStop;
  }
}

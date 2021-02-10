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

  private       boolean ballWaiting, isDone;
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

    ballWaiting   = false;
    isDone        = false;
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  //                                   |Shooter| ^
  //  |Front Intake|No Sensor|No Sensor|Waiting| |
  //   ------------------------------------>
  @Override
  public void execute()
  { 
    /* The running tally of balls doesn't matter to us in this command. In this command,
       we only need the number of balls in the Queue. If there is a ball in the shooter,
       we ignore it, if there's not, then we don't.
    */
    ballsInQueue = (Constants.ballInShooter) ? Constants.ballsControlled - 1 : Constants.ballsControlled;
    
    // Set local variables to reduce usage of loader subsystem methods and for readablity.
    ballWaiting  = (loader.ballWaiting());  // Ball Waiting next to the shooter
    
    // SmartDashboard.putString("DB/String 1", "Balls In Queue: " + ballsInQueue);
    // SmartDashboard.putString("DB/String 2", "Balls In System: " + Constants.ballsControlled);
    SmartDashboard.putNumber("Balls In Queue", ballsInQueue);
    SmartDashboard.putNumber("Balls In System", Constants.ballsControlled);
    // Logic for Belt Movement

    // First, is there a ball that can be loaded into the shooter?
    if (ballWaiting && !Constants.ballInShooter)
    {
      // Turn the STSMotor on and the Queue Motor on.
      loader.LoadBallMotorOn();
      loader.QueueMotorOn(0.5);
    }
    // If there isn't, but there is a ball waiting to be loaded, we are compacted.
    else if (ballWaiting || ballsInQueue == 0)
    {
      loader.QueueMotorOff();
      isDone = true;
    }
    // If the shooter is active, then start moving the queue.
    else if (Constants.shooterSystemActive)
      loader.QueueMotorOn(0.5);
    // If there is a ball just picked up, then move it.
    else if (ballsInQueue > 0)
      loader.QueueMotorOn(0.5);
    
    // Once the ball reaches the shooter, shut off the STSmotor.
    if (Constants.ballInShooter)
      loader.LoadBallMotorOff();
    
    // Did we interrupt this by pressing LB?
    if (loader.getStop())
    {
      isDone = true;
      loader.setStop(false);
    }
  }

  // ----------------------------------------------------------------------------
  // Stop when the conveyor has been compressed.
  @Override
  public boolean isFinished() 
  {
    return isDone;
  }
}
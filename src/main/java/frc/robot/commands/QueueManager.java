// BlitzCreek 3770 - OLLE 2021
// Queue Manager Command
// Compresses the Queue

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Import Constants
import frc.robot.Constants;

// Import Subsystems
import frc.robot.subsystems.Loader;

public class QueueManager extends CommandBase 
{
  private final Loader         loader;

  /* private     boolean        isDone; */
  private       int            ballsInQueue;
  
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
    // isDone   = false;
  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  //                                   |Shooter| ^
  //  |Front Intake|No Sensor|No Sensor|Waiting| |
  //   ------------------------------------>
  @Override
  public void execute()
  { 
    // Update Constants variables to reduce method calls and to let AwakenTheDragon
    // work without needing the loader subsystem.
    Constants.ballAtIntake = loader.ballAtIntake();
    Constants.ballWaiting  = loader.ballWaiting();

    SmartDashboard.putBoolean("Ball at Intake", Constants.ballAtIntake);
    SmartDashboard.putBoolean("Ball Waiting", Constants.ballWaiting);

    /* 
      The running tally of balls doesn't matter to us in this command. In this command,
      we only need the number of balls in the Queue. If there is a ball in the shooter,
      we ignore it, if there's not, then we don't.
    */
    ballsInQueue = (Constants.ballInShooter) ? Constants.ballsControlled - 1 : Constants.ballsControlled;
    
    // Prevent underflowing.
    if (ballsInQueue < 0)
      ballsInQueue = 0;

    // Set local variables to reduce usage of loader subsystem methods and for readablity.
    
    // SmartDashboard.putString("DB/String 1", "Balls In Queue: " + ballsInQueue);
    // SmartDashboard.putString("DB/String 2", "Balls In System: " + Constants.ballsControlled);
    SmartDashboard.putNumber("Balls In System",  ballsInQueue);
    SmartDashboard.putNumber("Balls Controlled", Constants.ballsControlled);
    
    // Logic for Belt Movement
    
    // First, is there a ball that can be loaded into the shooter?
    if (Constants.ballWaiting && !Constants.ballInShooter)
    {
      // Turn the STSMotor on and the Queue Motor on. 
      loader.LoadBallMotorOn();
      loader.QueueMotorOn(0.5);
    }
    // If there isn't, but there is a ball waiting to be loaded, we are compacted.
    else if (Constants.ballWaiting)
    {
      loader.QueueMotorOff();
      //loader.LoadBallMotorOff();
      // isDone = true;
    }
    // If the shooter is active, then start moving the queue.
    else if (Constants.shooterSystemActive)
      loader.QueueMotorOn(0.5);
    else if (ballsInQueue == 0)
      loader.QueueMotorOff();
    // If there is a ball just picked up, then move it.
    else if (ballsInQueue > 0)
      loader.QueueMotorOn(0.5);
    
    // Once the ball reaches the shooter, shut off the STSmotor.
    if (Constants.ballInShooter)
      loader.LoadBallMotorOff();

    // Did we interrupt this by pressing LB?
    // if (loader.getStop())
    // {
    //   loader.QueueMotorOff();
    //   loader.LoadBallMotorOff();
    //   isDone = true;
    //   loader.setStop(false);
    // }
  }

  // ----------------------------------------------------------------------------
  // Stop when the conveyor has been compressed.
  @Override
  public boolean isFinished() 
  {
    return false /*isDone*/;
  }
}

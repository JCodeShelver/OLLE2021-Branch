// BlitzCreek 3770 - OLLE 2021
// Front Intake Command
// Awakens the Dragon

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Loader;

public class QueueManager extends CommandBase 
{
  private final Loader      loader;
  
  private XboxController    controller;

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

  }

  // ----------------------------------------------------------------------------
  // Runs the front intake until a ball is picked up.
  @Override
  public void execute()
  { 

  }

  // ----------------------------------------------------------------------------
  // Stop when a ball is picked up.
  @Override
  public boolean isFinished() 
  {
    return true;
  }
}

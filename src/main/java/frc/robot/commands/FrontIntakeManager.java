// BlitzCreek 3770 - OLLE 2021
// Front Intake Command
// Allows player input of the Front Intake

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.FrontIntake;

public class FrontIntakeManager extends CommandBase 
{
  private final FrontIntake frontIntake;
  
  private XboxController    controller;
  
  private double            input;

  public FrontIntakeManager(FrontIntake f, XboxController c)
  {
    frontIntake = f;
    controller = c;

    addRequirements(frontIntake);
  }

  // ----------------------------------------------------------------------------
  // Initialization
  @Override
  public void initialize() 
  { 

  }

  // ----------------------------------------------------------------------------
  // Pipes in user input into the Front Intake Subsystem.
  @Override
  public void execute()
  { 
    if (frontIntake.isOut())
    {
      if (Math.abs(controller.getTriggerAxis(Hand.kRight)) > 0.2)
        input = controller.getTriggerAxis(Hand.kRight) * Math.abs(controller.getTriggerAxis(Hand.kRight));
      else
        input = 0;

      frontIntake.driveIntakeMotors(input);
    }
    else
      frontIntake.driveIntakeMotors(0.0);
  }

  // ----------------------------------------------------------------------------
  // This is a default command, so always return false.
  @Override
  public boolean isFinished() 
  {
    return false;
  }
}

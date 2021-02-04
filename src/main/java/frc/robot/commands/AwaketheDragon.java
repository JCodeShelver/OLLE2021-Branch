// BlitzCreek 3770 - OLLE 2021
// Awake the Dragon Command
// Manages the front intake when initiated.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.XboxController;

// Import Subsystems
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.Loader;

public class AwaketheDragon extends CommandBase 
{
    private final FrontIntake frontIntake;
    private final Loader      loader;

    private XboxController    controller;

    public AwaketheDragon(FrontIntake f, Loader l)
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
        frontIntake.drive(0.75);
    }

    // ----------------------------------------------------------------------------
    // Stop once the sensor is tripped.
    @Override
    public void execute()
    { 
        if (loader.ballAtIntake())
        {
            frontIntake.drive(0);
            loader.MovingMotorOn(0.5);
        }
    }

    // ----------------------------------------------------------------------------
    // Determines when to stop moving the front intake motors.
    @Override
    public boolean isFinished() 
    {
        return (!frontIntake.isOut() || frontIntake.isDisabled() || loader.ballAtIntake());
    }
}

// BlitzCreek 3770 - OLLE 2021
// Auton Command
// Basic Auton

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.subsystems.DriveSystem;

public class Auton extends CommandBase
{
    private final DriveSystem driveSystem;   // Reference to drive system object 
    private Timer driveTimer = new Timer();

    private double DRIVE_TIME = 2.0;    // Duration of action

    public Auton(DriveSystem d)
    {
        driveSystem = d;
        addRequirements(driveSystem);
    }

	// ----------------------------------------------------------------------------
    // Initiate shooting by starting action timer
    @Override
    public void initialize() 
    { 
        driveSystem.zeroEncoder();
        driveSystem.toggleScale();
        driveTimer.reset();
        driveTimer.start();
    }

    // ----------------------------------------------------------------------------
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if (driveTimer.get() < DRIVE_TIME)
            driveSystem.drive(-0.5, -0.5);
        else
            driveSystem.drive(0.0, 0.0);
    }

    // ----------------------------------------------------------------------------
    // Return true when timer reaches designated target time.
    @Override
    public boolean isFinished() 
    {
        if (driveTimer.get() >= DRIVE_TIME)
        {
            driveSystem.toggleScale();
            return true;
        } else
            return false;
    }
}
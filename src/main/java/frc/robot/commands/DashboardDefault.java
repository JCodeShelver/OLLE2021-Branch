// FRC Team 3770 - BlitzCreek - OLLE 2021
// Shooter (Default Actions) Command
// Manages shooter mechanism when not
// explicitly used.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Import Subsystems
import frc.robot.subsystems.Loader;

import frc.robot.Constants;

public class DashboardDefault extends CommandBase
{
    // Set vars
    private final Loader    loader;
   
    private       int       ballsInQueue;
    
    // --------------------------------------------------------------------------
    // Constructor
    public DashboardDefault(Loader l)
    {
        // Capture references to existing robot subsystems.  Define them as requirements.
        loader = l;

        addRequirements(loader);
    }

    // --------------------------------------------------------------------------
    // Initialization
    public void initialize() 
    {
        
    }
    
    // --------------------------------------------------------------------------
    // 
    public void execute() 
    {
        ballsInQueue = (Constants.ballInShooter) ? Constants.ballsControlled - 1 : Constants.ballsControlled;

        SmartDashboard.putNumber("Balls In System", ballsInQueue);
        SmartDashboard.putNumber("Balls Controlled", Constants.ballsControlled);
    }
    
    // --------------------------------------------------------------------------
    // 
    public boolean isFinished() 
    {
        return false;
    }
}
// FRC Team 3770 - BlitzCreek - OLLE 2021
// Shooter (Default Actions) Command
// Manages shooter mechanism when not
// explicitly used.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Import Subsystems
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.VisionPID;

public class ShootDefaultActions extends CommandBase
{
    // Set vars
    private final Shooter    shooter;
    private final VisionPID  visionPID;
    
    public double            Distance, RPM;

    // --------------------------------------------------------------------------
    // Constructor
    public ShootDefaultActions(Shooter s, VisionPID v)
    {
        // Capture references to existing robot subsystems.  Define them as requirements.
        shooter     = s;
        visionPID   = v;

        addRequirements(shooter, visionPID);
    }

    // --------------------------------------------------------------------------
    // Initialization
    public void initialize() 
    {        
        visionPID.LEDoff();
    }
    
    // --------------------------------------------------------------------------
    // 
    public void execute() 
    {
        shooter.updateBallInShooter();
        //shooter.stop();
        SmartDashboard.putBoolean("Can Shoot", false);
    }
    
    // --------------------------------------------------------------------------
    // 
    public boolean isFinished() 
    {
        return false;
    }
}
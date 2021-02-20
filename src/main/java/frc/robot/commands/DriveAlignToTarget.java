// FRC Team 3770 - BlitzCreek - OLLE 2021
// Drive Align-to-Target Command
// Command that can control the DriveSystem 
// subsystem and use encoder measure to 
// pivot turn to a desired angle measure.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;

// Import Subsystems
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.VisionPID;

// Import Constants
import frc.robot.Constants;

public class DriveAlignToTarget extends CommandBase
{
    // Set vars
    private final DriveSystem   driveSystem;  
    private final VisionPID     visionPID;

    private final Joystick rightStick  = new Joystick(Constants.RIGHT_STICK_USB_PORT);

    private boolean doneTurning;
    
    // For adjusting left/right motors for angle correction
    private double angleRotateMotorAdjust, idleTurnSpeed;
    
    // --------------------------------------------------------------------------
    // Constructor:  Capture time and motor level for straight drive
    public DriveAlignToTarget(DriveSystem d, VisionPID v) 
    {
        // Capture references to existing robot subsystems.  Define them as requirements.
        driveSystem   = d;   
        visionPID     = v;  

        addRequirements(driveSystem);
    }

    // --------------------------------------------------------------------------
    // Initialization
    public void initialize() 
    {
        doneTurning   = false;
        idleTurnSpeed = 0.4;
        
        visionPID.enable();
    }
    
    // --------------------------------------------------------------------------
    // During periodic action, robot controls drive system and
    // Spins until target acquired.
    public void execute() 
    {
        visionPID.LEDon();
        double xVisionTarget = visionPID.getMeasurement();
        System.out.println("XVISIONTARGET: " + xVisionTarget);
        
        {
            angleRotateMotorAdjust = visionPID.getOutput();
            System.out.println("PID Output: " + angleRotateMotorAdjust);
            
            /* 
                Adjust left/right motor sets to PID output.  Rotate as
                needed toward target angle (they are opposite signs)
                to make the wheels spin in opposite directions.
            */

            double left  = (+angleRotateMotorAdjust * 0.5) + rightStick.getY()/4;
            double right = (-angleRotateMotorAdjust * 0.5) + rightStick.getY()/4;

            driveSystem.drive(left, right);
        }
    }

    // --------------------------------------------------------------------------
    //
    @Override
    public void end(boolean interrupted)
    {
        driveSystem.drive(0.0, 0.0);
        visionPID.LEDoff();
    }
    
    // --------------------------------------------------------------------------
    // Routine stops when target in view and within range.
    // Vision system returns positive x-value when in view.
    // Then, stop when absolute error within tolerance.
    public boolean isFinished() 
    {
        return false;
    }
}

// FRC Team 3770 - BlitzCreek - OLLE 2021
// GyroPID Subsystem
// Manages gyroscope mechanism and PID 
// control with gyroscope.

/*
So the six degrees of freedom that a 3D object
can move in (that is, along the x, y, and z 
axes and rotating through them) have special
names that are used to avoid confusion.

             Y    Z
             ^   x
             |  /
             | /
<----------Object---------> X
           / | 
          /  |
         x   V

Forward and backward is called SURGE (think electricity surging through a cable).
Left and right is called SWAY (think swaying side to side).
Up and down is called HEAVE (think of your chest heaving as you breathe).
YAW is rotation about the Y-axis (think turning around when standing up).
PITCH is rotation about the X-axis (think falling forward).
ROLL is rotation about Z-axis (think doing a cartwheel).
*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.SPI;

// Import External Libraries
import com.kauailabs.navx.frc.AHRS;

// Import Constants
import frc.robot.Constants;

public class GyroPID extends PIDSubsystem 
{ 
	// Set vars
	private AHRS   gyro;
	
	private double currentSetpoint, pidOutput;

	// --------------------------------------------------------------------------
	// Constructor
	public GyroPID() 
	{
		super(new PIDController(Constants.GYRO_PID_P, Constants.GYRO_PID_I, Constants.GYRO_PID_D));   

    // Initialize gyroscope
    try
    {
			gyro = new AHRS(SPI.Port.kMXP);
			resetGyro();
    }
    catch (RuntimeException ex)
    {
      System.out.println("Error instantiating navX MXP:  " + ex.getMessage());
    }
    
		getController().setTolerance(Constants.GYRO_PID_TOLERANCE);   // Degree tolerance for set point
	}

	// --------------------------------------------------------------------------
	// Returns Yaw measurement of Gyroscope in degrees.
	@Override
	public double getMeasurement() 
	{
		// System.out.println("Gyro Angle: " + gyro.getAngle());
		return gyro.getAngle();
	}
	
	// --------------------------------------------------------------------------
	// Retrieves the calculated PID output.
	public double getOutput()
	{
		return pidOutput;
	}
	
	// --------------------------------------------------------------------------
	// Retrieves the current setpoint.
	public double getSetpoint()
	{
		return currentSetpoint;
	}		
	
	// --------------------------------------------------------------------------
	// Sets the Gyroscope's Yaw reading to 0 degrees.
	public void resetGyro()
	{	
		gyro.reset();
		gyro.zeroYaw();
	}
	
	// --------------------------------------------------------------------------
	// Adjusts the Proportion value of the PID controller.
	public void setPvalue(double newP)
	{
		getController().setP(newP);
	}

	// --------------------------------------------------------------------------
	// Adjusts the Integral value of the PID controller.
	public void setIvalue(double newI)
	{
		getController().setI(newI);
	}

	// --------------------------------------------------------------------------
	// Consumes output of PID controller using current set point.
	@Override
	public void useOutput(double output, double setpoint)
	{
		currentSetpoint = setpoint;
		pidOutput       = output;
	}
}	

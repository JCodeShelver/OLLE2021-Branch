// BlitzCreek 3770 - OLLE 2021
// Robot File
// Starts Robot and executes code by
// current mode.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot
{
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  @Override
  public void robotInit() 
  {
    // Declare Smart Dashboard entrys on startup
    // Priority Variables

    SmartDashboard.putNumber("Gyro Angle", 0.0);
    SmartDashboard.putNumber("Shooter RPM", 0.0);
    SmartDashboard.putNumber("Distance from Target", 0.0);
    SmartDashboard.putNumber("Balls Controlled", 0.0);
    SmartDashboard.putNumber("Balls In System", 0.0);
    //SmartDashboard.putBoolean("Ball In Shooter", false);
    //SmartDashboard.putBoolean("Intake Pneumatic", false);
    
     // Lower Priority Variables
    SmartDashboard.putNumber("Vision X", 0.0);
    SmartDashboard.putNumber("Vision Y", 0.0);
    SmartDashboard.putNumber("Vision Area", 0.0);
  
    // Initiate USB Cameras 1 and 2
    CameraServer.getInstance().startAutomaticCapture();
    CameraServer.getInstance().startAutomaticCapture();

    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() 
  {
    CommandScheduler.getInstance().run();
    CommandScheduler.getInstance().onCommandInitialize(command -> System.out.println("Command Initializing: " + command.getName()));
    CommandScheduler.getInstance().onCommandExecute(command -> System.out.println("Command Executing: " + command.getName()));
    CommandScheduler.getInstance().onCommandFinish(command -> System.out.println("Command Finishing: " + command.getName()));
    CommandScheduler.getInstance().onCommandInterrupt(command -> System.out.println("Command Interrupted: " + command.getName()));
  }

  @Override
  public void disabledInit()
  {
    Constants.currMode = Constants.Mode.DISABLED;
  }

  @Override
  public void disabledPeriodic() 
  {

  }

  @Override
  public void autonomousInit() 
  {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null)
    {
      m_autonomousCommand.schedule();
    }

    Constants.currMode = Constants.Mode.AUTONOMOUS;
  }

  @Override
  public void autonomousPeriodic()
  {

  }

  @Override
  public void teleopInit() 
  {
    if (m_autonomousCommand != null)
    {
      m_autonomousCommand.cancel();
    }

    Constants.currMode = Constants.Mode.TELEOP;
  }

  @Override
  public void teleopPeriodic()
  {

  }

  @Override
  public void testInit()
  {
    CommandScheduler.getInstance().cancelAll();
    Constants.currMode = Constants.Mode.TEST;
  }

  @Override
  public void testPeriodic() 
  {
    
  }
}

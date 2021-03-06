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
    SmartDashboard.putNumber("GSC Iteration", 0.0);
    SmartDashboard.putNumber("GSC Functioning Iteration", 0.0);
    
    //SmartDashboard.putBoolean("Ball In Shooter", false);
    //SmartDashboard.putBoolean("Intake Pneumatic", false);
    
     // Lower Priority Variables
    SmartDashboard.putNumber("Vision X", 0.0);
    SmartDashboard.putNumber("Vision Y", 0.0);
    SmartDashboard.putNumber("Vision Area", 0.0);
  
    // Initiate USB Cameras 1 and 2
    CameraServer.getInstance().startAutomaticCapture();
    CameraServer.getInstance().startAutomaticCapture();

    // Allow user to specify starting number of balls.
    if (SmartDashboard.getNumber("Balls Controlled", 0.0) != 0.0)
      Constants.ballsControlled = (int) SmartDashboard.getNumber("Balls Controlled", 0.0);

    SmartDashboard.putBoolean("Front Out", true);

    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() 
  {
    CommandScheduler.getInstance().run();
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

  @Override
  public void simulationInit()
  {

  }

  @Override
  public void simulationPeriodic()
  {
    Constants.ballAtIntake    = SmartDashboard.getBoolean("Ball At Intake", Constants.ballAtIntake);
    Constants.ballCaught      = SmartDashboard.getBoolean("Ball Caught", Constants.ballCaught);
    Constants.ballWaiting     = SmartDashboard.getBoolean("Ball Waiting", Constants.ballWaiting);
    Constants.ballsControlled = (int) SmartDashboard.getNumber("Balls Controlled", Constants.ballsControlled);
  }
}

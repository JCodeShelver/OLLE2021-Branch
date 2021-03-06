// BlitzCreek 3770 - OLLE 2021
// Robot Container file
// Controlls Button bindings and 
// sets commands.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class RobotContainer
{
  private final Joystick                 leftStick          = new Joystick(Constants.LEFT_STICK_USB_PORT);
  private final Joystick                 rightStick         = new Joystick(Constants.RIGHT_STICK_USB_PORT);
  private final XboxController           controller         = new XboxController(Constants.CONTROLLER_USB_PORT);

  private final DriveSystem              driveSystem        = new DriveSystem();
  private final GyroPID                  gyroPID            = new GyroPID();
  private final VisionPID                visionPID          = new VisionPID();
  private final FrontIntake              frontIntake        = new FrontIntake();
  private final Loader                   loader             = new Loader();
  private final Shooter                  shooter            = new Shooter();

  private final SendableChooser<Command> autonCommandChoice = new SendableChooser<>();

  private       double                   autonAngle, autonStraightInches, autonStraightSpeed;

  public RobotContainer()
  {
    configureButtonBindings();

    driveSystem.setDefaultCommand(new DriveHuman(
      driveSystem,
      () -> -leftStick.getY(),
      () -> -rightStick.getY()));
    
    shooter.setDefaultCommand(new ShootDefaultActions(shooter, visionPID));
    loader.setDefaultCommand(new QueueManager(loader));
    frontIntake.setDefaultCommand(new AwakenTheDragon(frontIntake));
  }

  // ----------------------------------------------------------------------------
  // Define drive button interface control bindings
  private void configureButtonBindings() 
  {
    /*
    |------------------------------------------------------------------------------------------------------------------------------------+
    |                                                   ___  _         ___                                                               |
    |                                                  / _ )(_)__  ___/ (_)__  ___ ____                                                  |
    |                                                 / _  / / _ \/ _  / / _ \/ _ `(_-<                                                  |
    |                                                /____/_/_//_/\_,_/_/_//_/\_, /___/                                                  |
    |                                                                        /___/                                                       |
    |--------------+-------------------+-----------------------+----------------------------+--------------------------------------------+
    |              |      Buttons      |     Left Joystick     |       Right Joystick       |                  Xbox Controller           |
    |--------------+-------------------+-----------------------+----------------------------+--------------------------------------------+
    |    ______    |1       /        A | NOT BOUND             | NOT BOUND                  | Front Intake Motors                     TOP|
    |   /_____//\  |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |/     \//| |2       /        B | Shoot Ball          OP| Slow Shooter Speed       OH| NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   B   |/| |3       /        X | Switch Camera Mode TOP| NOT BOUND                  | Start Shooter                            OP|
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |4       /        Y | NOT BOUND             | NOT BOUND                  | Move Front Intake                       TOP|
    |  |   U   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |5       /       LB | Toggle Speed Scale TOP| NOT BOUND                  | Manual Shooter Stop                      OP|
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   T   |/| |6       /       RB | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------
    |  |       |/| |7       /     Back | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |   T   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |8       /    Start | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   O   |/| |9       /       LS | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |10      /       RS | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |   N   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |11      /      N/A | NOT BOUND             | Toggle Speed Range      TOP| NOT APPLICABLE                             |
    |  \\_____///  |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |   ```````    |12      /      N/A | NOT BOUND             | NOT BOUND                  | NOT APPLICABLE                             |
    |==============+===================+=======================+============================+============================================+
    |==============+===========================+=================================+===========================+==========================================+
    |    _______   |           Axes            |            Continued            |         Continued         |         Continued                        |
    |   /_____//\  |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |  |/     \//| |1  /  X  /   Left X        | NOT BOUND                       | NOT BOUND                 | NOT BOUND                                |    
    |  |   T   |/| |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |  |   R   |/| |2  /  Y  /   Left Y        | D.Human, D.Target, Moving motor | D.Human                   | NOT BOUND                                |
    |  |   I   |/| |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |  |   G   |/| |3  /  Z  /  LT & RT        | Shooter RPM Tweak               | D.Human                   | Front Intake motors (LT - / RT +)        |
    |  |   G   |/| |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |  |   E   |/| |4  /  Throttle  /  Right X | NOT BOUND                       | NOT BOUND                 | NOT BOUND                                |
    |  |   R   |/| |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |  |   S   |/| |5  /  Hat X  /  Right Y    | NOT BOUND                       | NOT BOUND                 | NOT BOUND                                |
    |  \\_____///  |---------------------------+---------------------------------+---------------------------+------------------------------------------+
    |    ``````    |6  /  Hat Y  /  D-Pad      | NOT BOUND                       | NOT BOUND                 | NOT BOUND                                |
    |---------------------------------------------------------------------------------------------------------------------------------------------------+
    */
    // Left Stick 2 is reserved and used in StartTheLauncher. DO NOT BIND IT HERE!
    new JoystickButton(leftStick, 3).whenPressed(() -> visionPID.cameraModeSwitch()); // Toggle limelight camera mode.
    new JoystickButton(leftStick, 5).whenPressed(() -> driveSystem.toggleScale()); // Toggle Linear and Quadratic
    // new JoystickButton(leftStick, 6).whenPressed(() -> visionPID.lightModeSwitch()); // Toggle lights on limelight.
    
    // Right Stick 2 is reserved and used in StartTheLauncher. DO NOT BIND IT HERE!
    new JoystickButton(rightStick, 4).whenPressed(new DriveStraight(driveSystem, gyroPID, 0.33, -120.0, 360.0));
    new JoystickButton(rightStick, 5).whenPressed(new DriveStraight(driveSystem, gyroPID, 0.33, 120.0, 360.0));

    // new JoystickButton(rightStick, 5).whenPressed(() -> loader.ballCountUp()); // Temporary method to "catch" a ball.
    // new JoystickButton(rightStick, 6).whenPressed(() -> loader.ballCountDown()); // Temporary method to "uncatch" a ball.
    new JoystickButton(rightStick, 11).whenPressed(() -> driveSystem.toggleSpeed()); // Toggle between full and half speed.
    
    // new JoystickButton(controller, XboxController.Button.kA.value).toggleWhenPressed(new AwakenTheDragon(frontIntake));
    new JoystickButton(controller, XboxController.Button.kX.value).whenPressed(new StartTheLauncher(shooter, visionPID));
    // new JoystickButton(controller, XboxController.Button.kY.value).whenPressed(() -> frontIntake.move());
    new JoystickButton(controller, XboxController.Button.kBumperLeft.value).whenPressed(() -> shooter.mstop()); 
  }

  // ----------------------------------------------------------------------------
  // This is our Auton Command.
  public Command getAutonomousCommand()
  {
    // Zero Gyro here, in case it didn't zero on redeploy of code.
    gyroPID.resetGyro();
    
    autonAngle          = SmartDashboard.getNumber("AutonAngle", 0.0);
    autonStraightInches = SmartDashboard.getNumber("AutonStraightInches", 30);
    autonStraightSpeed  = SmartDashboard.getNumber("AutonStraightSpeed", 0.33);
    
    return null;
  }
}

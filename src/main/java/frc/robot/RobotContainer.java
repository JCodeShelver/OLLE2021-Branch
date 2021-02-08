// BlitzCreek 3770 - OLLE 2021
// Robot Container file
// Controlls Button bindings and 
// sets commands.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class RobotContainer
{
  private final Joystick        leftStick   = new Joystick(Constants.LEFT_STICK_USB_PORT);
  private final Joystick        rightStick  = new Joystick(Constants.RIGHT_STICK_USB_PORT);
  private final XboxController  controller  = new XboxController(Constants.CONTROLLER_USB_PORT);

  private final DriveSystem     driveSystem = new DriveSystem();
  private final GyroPID         gyroPID     = new GyroPID();
  private final VisionPID       visionPID   = new VisionPID();
  private final FrontIntake     frontIntake = new FrontIntake();
  private final Loader          loader      = new Loader();
  private final Shooter         shooter     = new Shooter();

  public RobotContainer()
  {
    visionPID.LEDoff();
    configureButtonBindings();

    driveSystem.setDefaultCommand(new DriveHuman(
      driveSystem,
      () -> leftStick.getY(),
      () -> rightStick.getY()));
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
    |    ______    |1       /        A | NOT BOUND             | Front Intake Toggle      OP| NOT BOUND                                  |
    |   /_____//\  |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |/     \//| |2       /        B | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   B   |/| |3       /        X | NOT BOUND             | Switch Camera Mode       OP| NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |4       /        Y | NOT BOUND             | Prepare To Shoot        TOP| NOT BOUND                                  |
    |  |   U   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |5       /       LB | NOT BOUND             | Choke Shooter RPM        OH| NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   T   |/| |6       /       RB | NOT BOUND             | Align to Target         TOP| Stop Front Intake motors, Shoot Ball  OH/OH|
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |7       /     Back | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |   T   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |8       /    Start | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |   O   |/| |9       /       LS | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |       |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |10      /       RS | NOT BOUND             | NOT BOUND                  | NOT BOUND                                  |
    |  |   N   |/| |-------------------+-----------------------+----------------------------+--------------------------------------------+
    |  |       |/| |11      /      N/A | NOT BOUND             | Half Drive Inputs        OH| NOT APPLICABLE                             |
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
    new JoystickButton(leftStick, 5).whenPressed(() -> driveSystem.toggleMode());
    new JoystickButton(leftStick, 6).whenPressed(() -> visionPID.lightModeSwitch());
    
    new JoystickButton(rightStick, 5).whenPressed(() -> loader.ballCountUp());
    new JoystickButton(rightStick, 6).whenPressed(() -> loader.ballCountDown());
    new JoystickButton(rightStick, 11).whenPressed(() -> driveSystem.toggleSpeed());

    new JoystickButton(controller, XboxController.Button.kA.value).whenPressed(new AwakentheDragon(frontIntake, loader));
    new JoystickButton(controller, XboxController.Button.kX.value).whenPressed(new QueueManager(loader, shooter));
    new JoystickButton(controller, XboxController.Button.kY.value).whenPressed(() -> frontIntake.move());
    new JoystickButton(controller, XboxController.Button.kBumperLeft.value).whenPressed(() -> loader.stop());    
    new JoystickButton(controller, XboxController.Button.kBumperRight.value).whenPressed(() -> frontIntake.stop());    
  }

  public Command getAutonomousCommand()
  {
    Command autonCommandChoice = new Auton(driveSystem);
    return autonCommandChoice;
  }
}

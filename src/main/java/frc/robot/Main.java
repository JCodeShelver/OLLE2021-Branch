// BlitzCreek 3770 - OLLE 2021
// Main file
// Does things

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main 
{
  private Main()
  {

  }

  public static void main(String... args)
  {
    RobotBase.startRobot(Robot::new);
  }
}

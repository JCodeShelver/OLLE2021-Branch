// BlitzCreek 3770 - OLLE 2021
// Auton Command
// Basic Auton

/*

Plan for IRaH-GSC 2021

Place robot on B1 if doing red and on D1 if doing blue.
Angle the robot facing the first power cell.
Use the distance traveled (measure) to determine route.
Have code for each possibility and use it when route is determined.

Path Set A
  0   1   2   3   4   5   6   7   8   9  10  11  12
  |---|---|---|---|---|---|---|---|---|---|---|---|
  |###|   |   |   |   |   |   |   |   |   |   |###|
A |###|---|---|---|---|---R---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
B |###S---|---|---|---|---|---B---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
C |###|---|---R---|---|---|---|---|---B---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
D |###S---|---|---|---R---|---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
E |###|---|---|---|---|---B---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
  |---|---|---|---|---|---|---|---|---|---|---|---|

A-Red
- Angled toward C3 from B1 (gyro will read 0° at true -arctan(0.5)°)
$ Pick up ball after 30sqrt(5) inches
- Turn (0°)
- Pick up ball after 30sqrt(5) inches
- Turn -(arctan(0.5) + arctan(3))°
- Pick up ball after 30sqrt(10) inches
- Turn arctan(3)°
- Go straight for 150 inches

A-Blue
- Angled toward E6 from D1 (gyro will read 0° at true -arctan(0.2)°)
$ Pick up ball after 30sqrt(26) inches
- Turn -(arctan(0.2) + arctan(3))°
- Pick up ball after 30sqrt(10) inches
- Turn (arctan(3) + arctan(0.5))°
- Pick up ball after 30sqrt(5) inches
- Turn -(arctan(0.5))°
- Go straight for 60 inches

Path Set B
  0   1   2   3   4   5   6   7   8   9  10  11  12
  |---|---|---|---|---|---|---|---|---|---|---|---|
  |###|   |   |   |   |   |   |   |   |   |   |###|
A |###|---|---|---|---|---|---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
B |###S---|---R---|---|---|---R---B---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
C |###|---|---|---|---|---|---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
D |###S---|---|---|---R---B---|---|---|---B---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
E |###|---|---|---|---|---|---|---|---|---|---|###|
  |###|   |   |   |   |   |   |   |   |   |   |###|
  |---|---|---|---|---|---|---|---|---|---|---|---|

B-Red
- Angled toward B3 from B1 (gyro will read 0° at true 0°)
- Pick up ball after 60 inches
- Turn 45°
- Pick up ball after 60sqrt(2) inches
- Turn -90°
- Pick up ball after 60sqrt(2) inches
- Turn 45°
- Go straight for 120 inches

B-Blue
- Angled toward D6 from D1 (gyro will read 0° at true 0°)
- Pick up ball after 150 inches
- Turn -45°
- Pick up ball after 60sqrt(2) inches
- Turn 90°
- Pick up ball after 60sqrt(2) inches
- Turn -45°
- Go straight for 30 inches

Proposed Idea:
Have thresholds of distance traveled to see what scenario we are in.
If 30sqrt(5) inches or 30sqrt(26) inches give or take...0.5 inches,
  Turn PATH_POLARITY(PATH_SPECIFIC_ANGLE_1)°
  Go straight 30sqrt(5)  or 30sqrt(10) inches
  Turn -PATH_POLARITY(arctan(0.5) + arctan(3))°
  Pick up ball after 30sqrt(5) or 30sqrt(10) inches
  Turn PATH_POLARITY(PATH_SPECIFIC_FINISH_ANGLE)
  Go straight for UNIQUE_FINISH_DISTANCE

If 60 inches or 150 inches (give or take an inch)
  Turn PATH_POLARITY(45°)
  Pick up ball after 60sqrt(2) inches
  Turn -PATH_POLARITY(90°)
  Pick up ball after 60sqrt(2) inches
  Turn to 0°
  Go straight for UNIQUE_FINISH_DISTANCE

Path-Specific-Angle (enum perhaps?)
A-Red:  0
A-Blue: arctan(0.2) + arctan(3)

Path-Specific-Polarity
Red:  +
Blue: -

Path-Specific-Finish-Angle
A-Red:  arctan(3)
A-Blue: arctan(0.5)

Unique-Finish-Distance
A-Red:  150
A-Blue: 60
B-Red:  120
B-Blue: 30
*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
// Import Subsystems
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.GyroPID;
import frc.robot.subsystems.Loader;

public class Auton extends CommandBase
{
  private final DriveSystem driveSystem;
  private final FrontIntake frontIntake;
  private final GyroPID     gyroPID;
  private final Loader      loader;
  
  public Auton(DriveSystem d, FrontIntake f, GyroPID g, Loader l)
  {
    driveSystem = d;
    frontIntake = f;
    gyroPID     = g;
    loader      = l;

    addRequirements(driveSystem, frontIntake, gyroPID, loader);
  }

	// ----------------------------------------------------------------------------
  // 
  @Override
  public void initialize() 
  { 
    new ParallelCommandGroup(new AwakenTheDragon(frontIntake), 
                             new DriveStraight(driveSystem, gyroPID, 0.33, 60.0, 0.0));
    // Make some command to drive forward until ball picked up.
  }

  // ----------------------------------------------------------------------------
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    // If we just caught the first ball:
    if (Constants.ballsControlled == 1)
    {
      Constants.GSCPath = 1;
    }
    else
    {

    }
  }

  // ----------------------------------------------------------------------------
  // Return true when timer reaches designated target time.
  @Override
  public boolean isFinished() 
  {
    return true;
  }
}
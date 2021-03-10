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
import edu.wpi.first.wpilibj2.command.WaitCommand;

// Import Constants
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

  private       double      delayTime;
  private       int         iteration;
  
  public Auton(DriveSystem d, FrontIntake f, GyroPID g, Loader l)
  {
    driveSystem = d;
    frontIntake = f;
    gyroPID     = g;
    loader      = l;

    delayTime   = 3.0;
    iteration   = -1;

    addRequirements(driveSystem, frontIntake, gyroPID, loader);
  }

  // Current Plan:
  // Implementing above code should be easy.
  // The hard part is the first segment. How do I figure out when to stop?

	// ----------------------------------------------------------------------------
  // 
  @Override
  public void initialize() 
  { 
    driveSystem.zeroEncoder();
  }
  
  // ----------------------------------------------------------------------------
  // Called after the first 60 inches are driven and then does fancy stuff for 
  // the rest of Auton GSC.
  @Override
  public void execute()
  {
    iteration ++;
    
    if (iteration == 0)
    {
      // Start by driving 60 inches (actually 57ish) for opening segment.
      new DriveStraight(driveSystem, gyroPID, 0.33, 60.0, 0.0);
      new WaitCommand(delayTime);
    }
    else
    {
      // We don't know our path yet.
      if (Constants.GSCPath == 0)
      {
        // So we haven't picked  up any balls yet.
        if (Constants.ballsControlled == 0)
        { 
          // Go some more distance depending on which iteration of the loop we're on.
          switch (iteration)
          {
            // So we drove the first leg and didn't get a ball? Try the next stop.
            case 1:
              new DriveStraight(driveSystem, gyroPID, 0.33, (30.0 * Math.sqrt(5)) - 60, 0.0);
              break;
            // That didn't work? Oh well then. Try driving to the third possible first
            // stop.
            case 2:
              new DriveStraight(driveSystem, gyroPID, 0.33, 150.0 - (30.0 * Math.sqrt(5)), 0.0);
              break;
            // Harrumph. Go to the last stop then. Be that way.
            case 3:
              new DriveStraight(driveSystem, gyroPID, 0.33, (30.0 * Math.sqrt(26)) - 150.0, 0.0);
              break;
          }

          // After we drive to our next stop, wait so that we can pick up a ball if 
          // one is present.
          new WaitCommand(delayTime);
        }
        // If we just caught the first ball ()
        else if (Constants.ballsControlled == 1)
        {
          // First ball caught? Set our GSCPath variable.
          // B-red is 1, A-Red is 2, B-Blue is 3, A-Blue is 4.
          Constants.GSCPath = iteration;
        }
      }
      else
      {
        // When we have a set path, run set code for the iteration.
        switch (Constants.GSCPath)
        {
          // B-Red
          case 1:
          {
            /*
              The iteration var and GSCPath var get unsynced once
              GSCPath is defined, as we have to go through the 
              entire execute block again.
            */
            switch (iteration - 1)
            {
              case 1:
              {
                // B-Red second stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 60.0 * Math.sqrt(2), 45.0);
                new WaitCommand(delayTime);
                break;
              }
              case 2:
              {
                // B-Red third stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 60.0 * Math.sqrt(2), -90.0);
                new WaitCommand(delayTime);
                break;
              }
              case 3:
              {
                // B-Red final stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 120.0, 45.0);
                break;
              }
            }
            break;
          }
          // A-Red
          case 2:
          {
            switch (iteration - 1)
            {
              case 1:
              {
                // A-Red second stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 30.0 * Math.sqrt(5), 0.0);
                new WaitCommand(delayTime);
                break;
              }
              case 2:
              {
                // A-Red third stint.
                new DriveTurn(driveSystem, gyroPID, -Math.toDegrees(Math.atan(0.5) + Math.atan(3)));
                new DriveStraight(driveSystem, gyroPID, 0.33, 30.0 * Math.sqrt(10), 360.0);
                new WaitCommand(delayTime);
                break;
              }
              case 3:
              {
                // A-Red final stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 150.0, Math.toDegrees(Math.atan(3)));
                break;
              }
            }
            break;
          }
          // B-Blue
          case 3:
          {
            switch (iteration - 1)
            {
              case 1:
              {
                // B-Blue second stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 60.0 * Math.sqrt(2), -45.0);
                new WaitCommand(delayTime);
                break;
              }
              case 2:
              {
                // B-Blue third stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 60.0 * Math.sqrt(2), 90.0);
                new WaitCommand(delayTime);
                break;
              }
              case 3:
              {
                // B-Blue final stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 30.0, -45.0);
                break;
              }
            }
            break;
          }
          // A-Blue
          case 4:
          {
            switch (iteration - 1)
            {
              case 1:
              {
                // A-Blue second stint.
                new DriveTurn(driveSystem, gyroPID, -Math.toDegrees(Math.atan(0.2) + Math.atan(3)));
                new DriveStraight(driveSystem, gyroPID, 0.33, 30.0 * Math.sqrt(10), 360.0);
                new WaitCommand(delayTime);
                break;
              }
              case 2:
              {
                // A-Blue third stint.
                new DriveTurn(driveSystem, gyroPID, Math.toDegrees(Math.atan(0.5) + Math.atan(3)));
                new DriveStraight(driveSystem, gyroPID, 0.33, 30.0 * Math.sqrt(5), 360.0);
                new WaitCommand(delayTime);
                break;
              }
              case 3:
              {
                // A-Blue final stint.
                new DriveStraight(driveSystem, gyroPID, 0.33, 60.0, -Math.toDegrees(Math.atan(0.5)));
                break;
              }
            }
            break;
          }
        }
      }
    }
  }

  // ----------------------------------------------------------------------------
  // Return true when timer reaches designated target time.
  @Override
  public boolean isFinished() 
  {
    return (iteration - Constants.GSCPath == 3);
  }
}
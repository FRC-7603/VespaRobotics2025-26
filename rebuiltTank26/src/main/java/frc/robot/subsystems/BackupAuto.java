package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FuelSubsystem;

public class BackupAuto {
  private final DriveSubsystem m_driveSubsystem;
  private final FuelSubsystem m_fuel;

  public BackupAuto() {
      m_driveSubsystem = new DriveSubsystem();
      m_fuel = new FuelSubsystem();
  }

  public Command simpleAuto() {
      return Commands.sequence(

          // Step 1: step back 1.5 meters (approx)
          Commands.run(() -> m_driveSubsystem.driveArcade(-0.5, 0), m_driveSubsystem).withTimeout(2),
          m_driveSubsystem.driveStopCommand(),

          // Step 2: shoot
          m_fuel.shootingCommand(),
          new WaitCommand(1.5),

          // Step 3: step back
          Commands.run(() -> m_driveSubsystem.driveArcade(-0.5, 0), m_driveSubsystem).withTimeout(1),
          m_driveSubsystem.driveStopCommand(),

          // Step 4: turn 30°
          Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.4), m_driveSubsystem).withTimeout(0.5),
          m_driveSubsystem.driveStopCommand(),

          // Step 5: go
          Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          m_driveSubsystem.driveStopCommand(),

          // Step 6: turn
          Commands.run(() -> m_driveSubsystem.driveArcade(0, -0.4), m_driveSubsystem).withTimeout(0.7),
          m_driveSubsystem.driveStopCommand(),

          // Step 7: go
          Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          m_driveSubsystem.driveStopCommand(),

          // Step 8: intake
          m_fuel.groundIntakeCommand(),
          new WaitCommand(2),
          m_fuel.stopCommand(),

          // Step 9: turn 180
          Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.6), m_driveSubsystem).withTimeout(2),
          m_driveSubsystem.driveStopCommand(),

          // Step 10: go
          Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          m_driveSubsystem.driveStopCommand()
      );
  }
}

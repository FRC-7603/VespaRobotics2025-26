package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.DriveSubsystem;

public class DriveCommand extends Command {

    private IntSupplier m_direction;
    private DoubleSupplier m_time;
    private final DriveSubsystem m_drive;

    public DriveCommand(DriveSubsystem subsystem, IntSupplier direction, DoubleSupplier time) {
      m_drive = subsystem;
      m_direction = direction;
      m_time = time;
      addRequirements(m_drive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
      switch (m_direction.getAsInt()) {
        case 0:
          System.out.println("Forward");
          m_drive.driveArcade(0.5, 0);
          break;
        case 1:
          System.out.println("Backward");
          m_drive.driveArcade(-0.5, 0);
          break;
        case 2:
          System.out.println("Stop");
          m_drive.driveArcade(0, 0);
          break;
      }
    }

    @Override
    public boolean isFinished() {
        Timer.delay(m_time.getAsDouble());
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.driveArcade(0, 0);
    }
}

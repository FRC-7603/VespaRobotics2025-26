package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.FuelSubsystem;

public class FuelCommand extends Command {
    private IntSupplier m_mode;
    private DoubleSupplier m_time;
    private final FuelSubsystem m_fuel;
    private double m_startTime;

    public FuelCommand(FuelSubsystem subsystem, IntSupplier mode, DoubleSupplier time) {
        m_fuel = subsystem;
        m_mode = mode;
        m_time = time;
        addRequirements(m_fuel);
    }

    @Override
    public void initialize() {
        m_startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
      switch (m_mode.getAsInt()) {
        case 0: // Auto shoot – RPM-gated
          m_fuel.spoolShooter();
          if (m_fuel.isShooterAtSpeed()) {
              m_fuel.runFeeder();
          } else {
              m_fuel.stopFeeder();
          }
          break;
        case 1:
          m_fuel.groundIntake();
          break;
        case 2:
          m_fuel.groundOuttake();
          break;
        case 3: // Manual shoot – RPM-gated
          m_fuel.spoolShooter();
          if (m_fuel.isShooterAtSpeed()) {
              m_fuel.runFeeder();
          } else {
              m_fuel.stopFeeder();
          }
          break;
      }
    }

    @Override
    public boolean isFinished() {
        // Non-blocking timeout instead of Timer.delay()
        return (Timer.getFPGATimestamp() - m_startTime) >= m_time.getAsDouble();
    }

    @Override
    public void end(boolean interrupted) {
        m_fuel.Stop();
    }
}

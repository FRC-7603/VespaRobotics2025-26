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

    public FuelCommand(FuelSubsystem subsystem, IntSupplier mode, DoubleSupplier time) {
        m_fuel = subsystem;
        m_mode = mode;
        m_time = time;
        addRequirements(m_fuel);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
      switch (m_mode.getAsInt()) {
        case 0:
          System.out.println("Auto shoot running");
          m_fuel.autoShoot();
          break;
        case 1:
          System.out.println("Ground intake");
          m_fuel.groundIntake();
          break;
        case 2:
          System.out.println("Ground outtake");
          m_fuel.groundOuttake();
          break;
        case 3:
          System.out.println("Shoot");
          m_fuel.shoot();
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
        m_fuel.Stop();
    }
}

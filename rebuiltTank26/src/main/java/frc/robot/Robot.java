// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ControllerConstantsLetters;

import java.util.Optional;

public class Robot extends TimedRobot {

  private Command m_autonomousCommand;
  private static final String kDefaultAuto = "Default";
  private static final String kBackupAuto = "Backup Auto";
  private static final String kNoAuto = "No Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final RobotContainer m_robotContainer = new RobotContainer();

  public Robot() {
    // Autonomous Chooser
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Backup Auto", kBackupAuto);
    m_chooser.addOption("No Auto", kNoAuto);
    SmartDashboard.putData(m_chooser);

    // Alliance Color
    Optional<Alliance> ally = DriverStation.getAlliance();

    if (ally.isPresent()) {
      if (ally.get() == Alliance.Blue) {
        SmartDashboard.putString("Alliance Color", "#003399");
      }
      if (ally.get() == Alliance.Red) {
        SmartDashboard.putString("Alliance Color", "#ff5050");
      }
    }
    else {
      SmartDashboard.putString("Alliance Color", "#666666");
    }

    // Serve Elastic Config
    WebServer.start(5800, Filesystem.getDeployDirectory().getPath());
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Voltage", RobotController.getBatteryVoltage());
    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());



    // Dashboard Diagnostics
    // SmartDashboard.putNumber("CAN Utilization %", RobotController.getCANStatus().percentBusUtilization * 100.0);
    // SmartDashboard.putNumber("CPU Temperature", RobotController.getCPUTemp());
    // SmartDashboard.putBoolean("RSL", RobotController.getRSLState());
    SmartDashboard.putNumber("Speed", -DriverStation.getStickAxis(0, ControllerConstantsLetters.leftJoystickYAxis));
    SmartDashboard.putNumber("Turn", DriverStation.getStickAxis(0, ControllerConstantsLetters.rightJoystickXAxis));
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch(m_autoSelected) {
      case kBackupAuto:
        m_autonomousCommand = m_robotContainer.simpleAuto();
        break;
      case kDefaultAuto:
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        if (m_autonomousCommand != null) {
          m_autonomousCommand.schedule();
        }
        break;
      case kNoAuto:
        break;
      default:
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        if (m_autonomousCommand != null) {
          m_autonomousCommand.schedule();
        }
    }
  }

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}

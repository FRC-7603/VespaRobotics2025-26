// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot {

  // CAN IDs for drivetrain
  private final VictorSPX motorL2 = new VictorSPX(1);
  private final VictorSPX motorL1 = new VictorSPX(2);
  
  private final VictorSPX motorR2 = new VictorSPX(3);
  private final VictorSPX motorR1 = new VictorSPX(4);
  
  // CAN IDs for shooter
  private final VictorSPX bottomShooter = new VictorSPX(7);  
  private final VictorSPX topShooter = new VictorSPX(10);
  
  // Joystick
  private final Joystick stick = new Joystick(0);

  // Shooter button mappings
  private final int intakeButton = 5;
  private final int shootButton  = 6;

  // Shooter speeds
  private final double intakespeed = 0.67;   // adjust as needed
  private final double firingspeed = -1;  // negative if needed for outtake

  private Command m_autonomousCommand;
  private final RobotContainer m_robotContainer;

  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
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
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    // Drivetrain Control 
    double forward = stick.getRawAxis(0) / 3;   // left stick X
    double rotation = -stick.getRawAxis(5) / 3;   // right stick Y

    double leftSpeed = forward + rotation;
    double rightSpeed = forward - rotation;

    motorL1.set(ControlMode.PercentOutput, leftSpeed);
    motorL2.set(ControlMode.PercentOutput, leftSpeed);
    motorR1.set(ControlMode.PercentOutput, rightSpeed);
    motorR2.set(ControlMode.PercentOutput, rightSpeed);

    // Shooter Control 
    if (stick.getRawButton(intakeButton)) {
      // Intake: pull game piece in
      bottomShooter.set(ControlMode.PercentOutput, intakespeed);
      topShooter.set(ControlMode.PercentOutput, intakespeed);
    } 
    else if (stick.getRawButton(shootButton)) {
      // Shoot/outtake
      bottomShooter.set(ControlMode.PercentOutput, firingspeed);
      topShooter.set(ControlMode.PercentOutput, firingspeed);
    }
  else {
    // Stop shooters
    bottomShooter.set(ControlMode.PercentOutput, 0);
    topShooter.set(ControlMode.PercentOutput, 0);
  }
}

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

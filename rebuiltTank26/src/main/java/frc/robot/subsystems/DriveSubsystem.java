// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import com.revrobotics.spark.SparkBase.PersistMode;
//import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.DriveConstants.*;

public class DriveSubsystem extends SubsystemBase {
  private final SparkMax leftLeader;
  private final SparkMax leftFollower;
  private final SparkMax rightLeader;
  private final SparkMax rightFollower;

  private final SparkMaxConfig leftLeaderConfig;
  private final SparkMaxConfig leftFollowerConfig;
  private final SparkMaxConfig rightLeaderConfig;
  private final SparkMaxConfig rightFollowerConfig;

  private final DifferentialDrive drive;

  private final Alert m_motorTempNotFound = new Alert("Unable to read motor temperature! Using CIMs?", AlertType.kWarning);
  private final Alert m_overheatAlert = new Alert("Motor is overheating!", AlertType.kWarning);


  // constructor
  public DriveSubsystem() {

    // create brushless motors for drive
    leftLeader = new SparkMax(LEFT_LEADER_ID, MotorType.kBrushless);
    leftFollower = new SparkMax(LEFT_FOLLOWER_ID, MotorType.kBrushless);
    rightLeader = new SparkMax(RIGHT_LEADER_ID, MotorType.kBrushless);
    rightFollower = new SparkMax(RIGHT_FOLLOWER_ID, MotorType.kBrushless);

    SparkMaxConfig config = new SparkMaxConfig();

    // set up differential drive class
    drive = new DifferentialDrive(leftLeader, rightLeader);

    SendableRegistry.addChild(drive, leftLeader);
    SendableRegistry.addChild(drive, leftFollower);
    SendableRegistry.addChild(drive, rightLeader);
    SendableRegistry.addChild(drive, rightFollower);
    SendableRegistry.setName(drive, "Differential Drive");
    SmartDashboard.putData(drive);

    // Set can timeout. Because this project only sets parameters once on
    // construction, the timeout can be long without blocking robot operation. Code
    // which sets or gets parameters during operation may need a shorter timeout.
    leftLeader.setCANTimeout(250);
    rightLeader.setCANTimeout(250);
    leftFollower.setCANTimeout(250);
    rightFollower.setCANTimeout(250);

    // Create the configuration to apply to motors. Voltage compensation
    // helps the robot perform more similarly on different
    // battery voltages (at the cost of a little bit of top speed on a fully charged
    // battery). The current limit helps prevent tripping
    // breakers.

    leftLeaderConfig = new SparkMaxConfig();
    leftFollowerConfig = new SparkMaxConfig();
    rightLeaderConfig = new SparkMaxConfig();
    rightFollowerConfig = new SparkMaxConfig();

    leftLeaderConfig.voltageCompensation(12)
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)
                    .disableFollowerMode()
                    .inverted(false);

    leftFollowerConfig.voltageCompensation(12)
                      .idleMode(IdleMode.kBrake)
                      .smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)
                      .inverted(true)
                      .follow(LEFT_LEADER_ID);

    rightLeaderConfig.voltageCompensation(12)
                      .idleMode(IdleMode.kBrake)
                      .smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)
                      .disableFollowerMode()
                      .inverted(true);

    rightFollowerConfig.voltageCompensation(12)
                      .idleMode(IdleMode.kBrake)
                      .smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT)
                      .inverted(true)
                      .follow(RIGHT_LEADER_ID);

    leftLeader.configure(leftLeaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    leftFollower.configure(leftFollowerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    rightLeader.configure(rightLeaderConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    rightFollower.configure(rightFollowerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    // Remove following, then apply config to right leader
    //rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // Set config to inverted and then apply to left leader. Set Left side inverted
    // so that postive values drive both sides forward
    config.inverted(true);
    //leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // methods
  public void driveArcade(double xSpeed, double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
    SmartDashboard.putNumber("Arcade speed", xSpeed);
  }

  public void stop() {
    drive.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_overheatAlert.set(
      // Check either 4 motors for overheating. If any are above the threshold, trigger the alert
      leftLeader.getMotorTemperature() > DRIVE_MOTOR_OVERHEAT_TEMPERATURE || 
      rightLeader.getMotorTemperature() > DRIVE_MOTOR_OVERHEAT_TEMPERATURE || 
      leftFollower.getMotorTemperature() > DRIVE_MOTOR_OVERHEAT_TEMPERATURE || 
      rightFollower.getMotorTemperature() > DRIVE_MOTOR_OVERHEAT_TEMPERATURE
    );

    m_motorTempNotFound.set(
      // Check if any of the motors are reporting a temperature of 0, which is likely an error (CIMs don't report temp)
      leftLeader.getMotorTemperature() < 2 || 
      rightLeader.getMotorTemperature() < 2 || 
      leftFollower.getMotorTemperature() < 2 || 
      rightFollower.getMotorTemperature() < 2
      );
  }

}
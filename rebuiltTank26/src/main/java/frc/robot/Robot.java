// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpil.first.wpilibj.Timer;



//use tabs when code is part of class to make it more neat
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default: Backup from hub and then launch";
  private static final String kLanchFromSide = "Launch from side and stay";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  private Command m_autonomousCommand;
  private final RobotContainer m_robotContainer;

  private final SparkMax leftDriveLead = new SparkMax(1, MotorType.kBrushed);
  private final SparkMax leftDriveFollow = new SparkMax(2, MotorType.kBrushed);
  private final SparkMax rightDriveLead = new SparkMax(3, MotorType.kBrushed);
  private final SparkMax rightDriveFollow = new SparkMax(4, MotorType.kBrushed);

  private final SparkMax intakeAndLauncherRoller = new SparkMax(5, MotorType.kBrushed);
  private final SparkMax feederRoller = new SparkMax(6, MotorType.kBrushed);

  private final DifferentialDrive myDrive = new DifferentialDrive(leftDriveLead, rightDriveLead);

  private final Timer autoTimer = new Timer();

  //----------------- fuel mechanism paramaters ---------------
  //hey its like Boris' code during the lesson :)
  private static final double INTAKING INTAKE VOLATGE = 9;
  private static final double FEEDER INTAKE VOLATGE = -12;
  
  private static final double LAUNCHING LAUNCHER VOLATGE = 10.6;
  private static final double LAUNCHING FEEDER VOLATGE = 9;

  private static final double SPIN UP FEEDER VOLATGE = -6;
  private static final double SPIN UP SECONDS VOLATGE = 1;


  public Robot() {
    m_chooser.setDefaultOption("Default: Backup from hub and then launch", kDefaultAuto);
    m_chooser.addOption("Launch from side and stay", kLaunchFromSide);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_robotContainer = new RobotContainer();

    //------------------------ Drive Configs -----------------------
    SparkMaxConfig driveConfig = new SparkMaxConfig();
    driveConfig.voltageCompensation(12);
    driveConfig.smartCurrentLimit(60);
    //if something goes wrong it will trip out instead of repeating the mistake
    
    driveConfig.follow(leftDriveLead);
    //following motor ex. leftDriveFollow will follow leftDriveLead
    leftDriveFollow.configure(driveConfig, ResetMode.kResetSafeParamaters, PersistMode.kResetSafeParamaters);
    
    driveConfig.follow(rightDriveLead);
    rightDriveFollow.configure(driveConfig, ResetMode.kResetSafeParamaters, PersistMode.kResetSafeParamaters);

    driveConfig.disableFollowerMode();
    driveConfig.inverted(false);
    rightDriveLead.configure(driveConfig, ResetMode.kResetSafeParamaters, PersistMode.kPersistParamaters);

    driveConfig.inverted(true);
    lefttDriveLead.configure(driveConfig, ResetMode.kResetSafeParamaters, PersistMode.kPersistParamaters);


    //--------------------- Fuel/Lancher Configs-----------------
    SparkMaxConfig launcherConfig = new SparkMaxConfig();
    launcherConfig.smartCurrentLimit(60);
    launcherConfig.inverted(true);
    intakeAndLauncherRoller.configure(launcherConfig, ResetMode.kResetSafeParamaters, PersistMode.kPersistParamaters);

    SparkMaxConfig feederRoller = new SparkMaxConfig();
    feederConfig.smartCurrentLimit(60);
    feederRoller.configure(feederConfig, ResetMode.kResetSafeParamaters, PersistMode.kPersistParamaters);


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
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto Selected: " + m_autoSelected);
    autoTimer.start();
    autoTimer.reset();

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  //will run over and over again while in auto mode at start of match
    switch (m_autoSelected) {
      case kLanchFromSide: //Start from either side of hub aimed and launch ball, no driving
        if(autoTimer.get() < SPIN_UP_SECONDS){
          intakeAndLauncherRoller.setVoltage(LAUNCHING_LAUNCHER_VOLTAGE);
          feederRoller.setVoltage(SPIN_UP_FEEDER_VOLTAGE);
        }
        else if(autoTimer.get() < 11){
          intakeAndLauncherRoller.setVoltage(LAUNCHING_LAUNCHER_VOLTAGE);
          feederRoller.setVoltage(LAUNCHING_FEEDER_VOLTAGE);
        }
        else{ //turns everything off
          intakeAndLauncherRoller.setVoltage(0);
          feederRoller.setVoltage(0);
        }
      break;
      case kDefaultAuto: //start in front of goal and launch
      default:
        //default auto code:
        // leftDriveLead.set(.35);
        // rightDriveLead.set(.35);

        if(autoTimer.get() < 1){
          myDrive.tankDrive(.5, .5);
        }
        else{
          myDrive.tankDrive(0,0);
        }

        if(autoTimer.get() < SPIN_UP_SECONDS + .5){ //spin up the launcher
          intakeAndLauncher.setVoltage(LAUNCHING_LAUNCHER_VOLTAGE);
          feederRoller.setVoltage(SPIN_UP_FEEDER_VOLTAGE);
          myDrive.tankDrive(.4, .4);
        }
        else if(autoTimer.get() < 11){  //10 sec launching time
          intakeAndLauncherRoller.setVoltage(LAUNCHING_LAUNCHER_VOLTAGE);
          feederRoller.setVoltage(LAUNCHING_FEEDER_VOLTAGE);
          myDrive.tankDrive(0,0);
        }

        else{
          myDrive.tankDrive(0,0);
          intakeAndLauncherRoller.setVoltage(0);
          feederRoller.setVoltage(0);
        }

      break;
    }
  }

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
  //will run once at start of driver control mode
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}
  //where we put our driver control code

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}
//used to test a specific part of the code seperately

  @Override
  public void testExit() {}
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.ControllerConstants;

//DS
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

//Subsystems
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;
//import frc.robot.subsystems.AutoLockAprilTag;

public class RobotContainer {

  private final CommandXboxController joystickXBOX = new CommandXboxController(1);
  private final Joystick stick = new Joystick(0);

  public final Shooter shooter = new Shooter();
  public final Climber climber = new Climber();
  public final Intake intake = new Intake();
  public final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();
  public final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();
  public final VisionSubsystem visionSubsystem = new VisionSubsystem();
  //public final AutoLockAprilTag autoLockSubsystem = new AutoLockAprilTag(driveSubsystem, visionSubsystem);

  // // The operator's controller
  // private final CommandXboxController operatorController = new CommandXboxController(
  //     OPERATOR_CONTROLLER_PORT);

  // The operator's controller
  private final Joystick operatorController = new Joystick(OPERATOR_CONTROLLER_PORT);

  public RobotContainer() {
    // THIS IS FOR (By twice) PathPlanner but idk if we can use pathplanner cuz to encoders
    NamedCommands.registerCommand("Shoot", shooter.FuelInCommand());
    NamedCommands.registerCommand("ClimbUP", climber.ClimberSpeedUpCommand());
    NamedCommands.registerCommand("ClimbDown", climber.ClimberSpeedDownCommand());
    NamedCommands.registerCommand("FuelIn", intake.FuelInCommand());
    //NamedCommands.registerCommand("fuelsystem", getAutonomousCommand());
    //NamedCommands.registerCommand("setIntakeLauncherRoller", fuelSubsystem.setIntakeLauncherRoller(60));
  
    configureBindings();
    configureDefaultCommands();
    }
  
//LINE 67

  private void configureBindings() {
  
    // Buttons (change numbers if needed)
    JoystickButton leftButton  = new JoystickButton(stick, ControllerConstants.INTAKE);
    JoystickButton rightButton = new JoystickButton(stick, ControllerConstants.SHOOTER);
    JoystickButton DynamicOutTake = new JoystickButton(stick, ControllerConstants.DynamicOutTake);
    //JoystickButton autoLockButton = new JoystickButton(stick, 7);
  
    //this is for Xbox (THIS IS FOR BY TWICE!?!)
    leftButton.onTrue(shooter.FuelInCommand());
    rightButton.onTrue(shooter.FuelOutCommand());

    // Hold button to activate auto-lock
    //autoLockButton.onTrue
    //autoLockButton.onFalse
  }
  
  private void configureDefaultCommands() {
  
    // Arcade drive using joystick
    driveSubsystem.setDefaultCommand(
      Commands.run(
        () -> driveSubsystem.driveArcade(
          -stick.getY(), // forward/back
          stick.getX()   // turn
        ),
      driveSubsystem
      )
    );
  }

  public Command getAutonomousCommand() {

    // Optional<Alliance> al = DriverStation.getAlliance();
    // if(al.isPresent()){
    //     if(al.get() == Alliance.Blue){
    //        return new PathPlannerAuto("Far Left Auto");
    //     }
    //     if(al.get() == Alliance.Red){
    //         return new PathPlannerAuto("Far Left Auto RED");
    //     }
    // }
    // return new PathPlannerAuto("Far Left Auto");
    return null;
  }
}

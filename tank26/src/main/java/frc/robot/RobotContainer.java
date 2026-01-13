// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

// import com.pathplanner.lib.commands.PathPlannerAuto;
// import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import static frc.robot.Constants.OperatorConstants.*;

//DS
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

//Subsystems
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class RobotContainer {

  private final CommandXboxController joystickXBOX = new CommandXboxController(1);
  private final Joystick stick = new Joystick(0);

  public final Shooter shooter = new Shooter();
  public final Climber climber = new Climber();
  public final Intake intake = new Intake();
  public final CANDriveSubsystem driveSubsystem = new CANDriveSubsystem();
  public final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();

  // // The operator's controller
  // private final CommandXboxController operatorController = new CommandXboxController(
  //     OPERATOR_CONTROLLER_PORT);

  // The operator's controller
  private final Joystick operatorController = new Joystick(OPERATOR_CONTROLLER_PORT);

  public RobotContainer() {
    NamedCommands.registerCommand("FuelIN", shooter.FuelInCommand());
    NamedCommands.registerCommand("FuelOUT", intake.FuelInCommand());
    //NamedCommands.registerCommand("Climber", climber.());
    //NamedCommands.registerCommand("setIntakeLauncherRoller", fuelSubsystem.setIntakeLauncherRoller(60));
  
    configureBindings();
    configureDefaultCommands();
    }
  
  private void configureBindings() {
  
    // Buttons (change numbers if needed) LINE 67
    JoystickButton leftButton  = new JoystickButton(stick, 5);
    JoystickButton rightButton = new JoystickButton(stick, 6);
  
    leftButton.onTrue(shooter.FuelInCommand());
    rightButton.onTrue(shooter.FuelOutCommand());
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

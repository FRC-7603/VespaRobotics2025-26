// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import com.pathplanner.lib.auto.NamedCommands;

// import com.pathplanner.lib.commands.PathPlannerAuto;
// import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;


public class RobotContainer {

  private final CommandXboxController joystickXBOX = new CommandXboxController(0);
  private final Joystick stick = new Joystick(0);

  public final Shooter shooter = new Shooter();
  public final Climber climber = new Climber();


  public RobotContainer() {
    NamedCommands.registerCommand("FuelIN", shooter.FuelInCommand());
    configureBindings();
  }

  private void configureBindings() {}

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

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
// import com.pathplanner.lib.auto.NamedCommands;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

//idk if we can use pathplanner cuz no encoders
// import com.pathplanner.lib.commands.PathPlannerAuto;
// import com.pathplanner.lib.auto.NamedCommands;

// import edu.wpi.first.wpilibj2.command.button.Trigger;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import static frc.robot.Constants.OperatorConstants.*;

// DS
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.DriverStation.Alliance;

// Subsystems
// import frc.robot.subsystems.VisionSubsystem;
// import frc.robot.subsystems.Climber;
import frc.robot.subsystems.FuelSubsystem;
import frc.robot.Constants.ControllerConstantsLetters;
import frc.robot.subsystems.DriveSubsystem;

public class RobotContainer {
  // the only controller that is used right now
  public final Joystick stick = new Joystick(0);
  // public final Climber m_climber = new Climber();
  public final FuelSubsystem m_fuel = new FuelSubsystem();
  public final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
  // public final VisionSubsystem m_visionSubsystem = new VisionSubsystem();

  // Reserved for future reference if AprilTags would work
  // public final AutoLockAprilTag autoLockSubsystem = new AutoLockAprilTag(driveSubsystem, visionSubsystem);

  // The operator's controller
  // private final CommandXboxController operatorController = new CommandXboxController(
  //     OPERATOR_CONTROLLER_PORT);

  // The operator's controller
  // private final Joystick operatorController = new Joystick(OPERATOR_CONTROLLER_PORT);

  // constructor
  public RobotContainer() {
    // THIS IS FOR (By twice)
    // Used for PathPlanner but idk if we can use pathplanner cuz to encoders
    // NamedCommands.registerCommand("Shoot", m_shooter.ShooterShootCommand());
    // NamedCommands.registerCommand("ClimbUp", m_climber.ClimberSpeedUpCommand());
    // NamedCommands.registerCommand("ClimbDown", m_climber.ClimberSpeedDownCommand());
    // NamedCommands.registerCommand("Intake", m_intake.intakeCommand());
    //NamedCommands.registerCommand("fuelsystem", getAutonomousCommand());
    //NamedCommands.registerCommand("setIntakeLauncherRoller", fuelSubsystem.setIntakeLauncherRoller(60));
  
    configureBindings();
    configureDefaultCommands();
    System.out.println("Robot started.");
    }


  private void configureBindings() {
  
    // Buttons (change numbers if needed)
    // JoystickButton DynamicOutTake = new JoystickButton(stick, ControllerConstants.DynamicOutTake);

    // JoystickButton aButton = new JoystickButton(stick, ControllerConstantsLetters.A);
    JoystickButton bButton = new JoystickButton(stick, ControllerConstantsLetters.B);
    JoystickButton xButton = new JoystickButton(stick, ControllerConstantsLetters.A);
    // JoystickButton yButton = new JoystickButton(stick, ControllerConstantsLetters.B);
    JoystickButton lbButton = new JoystickButton(stick, ControllerConstantsLetters.LB);
    JoystickButton rbButton = new JoystickButton(stick, ControllerConstantsLetters.RB);
    // JoystickButton backButton = new JoystickButton(stick, ControllerConstantsLetters.back);
    // JoystickButton startButton = new JoystickButton(stick, ControllerConstantsLetters.start);
    // JoystickButton leftJopAystickButton = new JoystickButton(stick, ControllerConstantsLetters.leftJoystickPress);
    // JoystickButton rightJoystickButton = new JoystickButton(stick, ControllerConstantsLetters.rightJoystickPress);

    //this is for Logitech (THIS IS FOR BY TWICE!?!)
    lbButton.whileTrue(m_fuel.groundIntakeCommand());
    rbButton.whileTrue(m_fuel.groundOuttakeCommand());
    xButton.whileTrue(m_fuel.shootingCommand());
    lbButton.whileFalse(m_fuel.stopCommand());
    rbButton.whileFalse(m_fuel.stopCommand());
    xButton.whileFalse(m_fuel.stopCommand());
    bButton.whileTrue(m_driveSubsystem.driveStopCommand());
    
    // Hold button to activate auto-lock
    //autoLockButton.onTrue
    //autoLockButton.onFalse
  }
  
  private void configureDefaultCommands() {
  
    // Drivetrain Subsystem
    m_driveSubsystem.setDefaultCommand(
      Commands.run(
        () -> m_driveSubsystem.driveArcade(
          -stick.getRawAxis(ControllerConstantsLetters.rightJoystickXAxis), // forward/back
          stick.getRawAxis(ControllerConstantsLetters.leftJoystickYAxis)   // turn
        ),
        m_driveSubsystem
      )
    );


    // Climber Subsystem
    // m_climber.setDefaultCommand(
      // Commands.run(() -> m_climber.ClimberStopCommand(), m_climber)
    // );


    // Intake Subsystem
    m_fuel.setDefaultCommand(
      Commands.run(() -> m_fuel.stopCommand(), m_fuel)
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

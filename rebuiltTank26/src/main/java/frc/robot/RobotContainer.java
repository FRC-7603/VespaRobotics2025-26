// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
// import com.pathplanner.lib.auto.NamedCommands;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.WaitCommand;
// DS
// import edu.wpi.first.wpilibj2.command.button.Trigger;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import static frc.robot.Constants.OperatorConstants.*;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

// Subsystems
import frc.robot.subsystems.VisionSubsystem;
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
  public final VisionSubsystem m_visionSubsystem = new VisionSubsystem();

  // The operator's controller
  // private final CommandXboxController operatorController = new CommandXboxController(
  //     OPERATOR_CONTROLLER_PORT);

  // The operator's controller
  // private final Joystick operatorController = new Joystick(OPERATOR_CONTROLLER_PORT);

  // constructor
  public RobotContainer() {
  
    configureBindings();
    configureDefaultCommands();
    System.out.println("Robot started.");
    }


  private void configureBindings() {
  
    // Buttons (change numbers if needed)
    // JoystickButton DynamicOutTake = new JoystickButton(stick, ControllerConstants.DynamicOutTake);

    // JoystickButton aButton = new JoystickButton(stick, ControllerConstantsLetters.A);
    JoystickButton bButton = new JoystickButton(stick, ControllerConstantsLetters.B);
    JoystickButton xButton = new JoystickButton(stick, ControllerConstantsLetters.X);
    // JoystickButton yButton = new JoystickButton(stick, ControllerConstantsLetters.Y);
    JoystickButton lbButton = new JoystickButton(stick, ControllerConstantsLetters.LB);
    JoystickButton rbButton = new JoystickButton(stick, ControllerConstantsLetters.RB);
    // JoystickButton backButton = new JoystickButton(stick, ControllerConstantsLetters.back);
    // JoystickButton startButton = new JoystickButton(stick, ControllerConstantsLetters.start);
    // JoystickButton leftJopAystickButton = new JoystickButton(stick, ControllerConstantsLetters.leftJoystickPress);
    // JoystickButton rightJoystickButton = new JoystickButton(stick, ControllerConstantsLetters.rightJoystickPress);

    //this is for Logitech (THIS IS FOR BY TWICE!?!)
    lbButton.onTrue(m_fuel.groundIntakeCommand());
    rbButton.onTrue(m_fuel.groundOuttakeCommand());
    xButton.onTrue(m_fuel.shootingCommand());
    lbButton.onFalse(m_fuel.stopCommand());
    rbButton.onFalse(m_fuel.stopCommand());
    xButton.onFalse(m_fuel.stopCommand());

    bButton.whileTrue(m_fuel.lebron2().withTimeout(0.5).andThen(m_fuel.lebron1()).finallyDo(() -> m_fuel.stopCommand()));
    bButton.whileFalse(m_fuel.stopCommand());
    // Hold button to activate auto-lock
    //bButton.onTrue(m_visionSubsystem.turnToTarget());
  }
  
  private void configureDefaultCommands() {
  
    // Drivetrain Subsystem
    m_driveSubsystem.setDefaultCommand(
      Commands.run(
        () -> m_driveSubsystem.driveArcade(
          stick.getRawAxis(ControllerConstantsLetters.leftJoystickYAxis), // forward/back
          stick.getRawAxis(ControllerConstantsLetters.rightJoystickXAxis)   // turn

          //stick.getRawAxis(ControllerConstantsLetters.leftJoystickYAxis), // forward/back
          //stick.getRawAxis(ControllerConstantsLetters.rightJoystickXAxis)   // turn
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

     System.out.println("AUTO START");

      m_visionSubsystem.setTargetTag(7);

      return m_visionSubsystem.driveToTagLEBRON(m_driveSubsystem, 1.5);

      //return simpleAuto();

  }


public Command simpleAuto() {
    return Commands.sequence(

        // Step 1: step back 1.5 meters (approx)
        Commands.run(() -> m_driveSubsystem.driveArcade(-0.5, 0), m_driveSubsystem).withTimeout(2),
        m_driveSubsystem.driveStopCommand(),

        // Step 2: shoot
        m_fuel.shootingCommand(),
        new WaitCommand(1.5),

        // Step 3: step back
        Commands.run(() -> m_driveSubsystem.driveArcade(-0.5, 0), m_driveSubsystem).withTimeout(1),
        m_driveSubsystem.driveStopCommand(),

        // Step 4: turn 30°
        Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.4), m_driveSubsystem).withTimeout(0.5),
        m_driveSubsystem.driveStopCommand(),

        // Step 5: go
        Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
        m_driveSubsystem.driveStopCommand(),

        // Step 6: turn
        Commands.run(() -> m_driveSubsystem.driveArcade(0, -0.4), m_driveSubsystem).withTimeout(0.7),
        m_driveSubsystem.driveStopCommand(),

        // Step 7: go
        Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
        m_driveSubsystem.driveStopCommand(),

        // Step 8: intake
        m_fuel.groundIntakeCommand(),
        new WaitCommand(2),
        m_fuel.stopCommand(),

        // Step 9: turn 180
        Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.6), m_driveSubsystem).withTimeout(2),
        m_driveSubsystem.driveStopCommand(),

        // Step 10: go
        Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
        m_driveSubsystem.driveStopCommand()
    );
}

}

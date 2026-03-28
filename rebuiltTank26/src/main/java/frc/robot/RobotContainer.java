// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
// import com.pathplanner.lib.auto.NamedCommands;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

// DS + logitech controller
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import edu.wpi.first.wpilibj2.command.button.Trigger;

// Subsystems
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FuelSubsystem;

// Commands
import frc.robot.commands.DriveCommand;
import frc.robot.commands.FuelCommand;

// Controllers
import frc.robot.Constants.ControllerConstantsLetters;
import frc.robot.Constants.XBOXControllerConstantsLetters;


public class RobotContainer {
  // the only controller that is used right now
  public final Joystick stick = new Joystick(0);
  public final XboxController xboxController = new XboxController(0);
  // public final Climber m_climber = new Climber();
  public final FuelSubsystem m_fuel = new FuelSubsystem();
  public final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
  // public final VisionSubsystem m_visionSubsystem = new VisionSubsystem();

  // The operator's controller
  // private final Joystick operatorController = new Joystick(OPERATOR_CONTROLLER_PORT);

  // constructor
  public RobotContainer() {
  
    //configureBindings();     // Logitech
    configureBindingsXBOX();   // Xbox
    configureDefaultCommands();
    System.out.println("Robot started.");
    }


  private void configureBindingsXBOX() {

    JoystickButton aButton = new JoystickButton(xboxController, XBOXControllerConstantsLetters.A);
    //JoystickButton yButton = new JoystickButton(xboxController, ControllerConstantsLetters.Y);
    JoystickButton bButton = new JoystickButton(xboxController, XBOXControllerConstantsLetters.B);
    JoystickButton xButton = new JoystickButton(xboxController, XBOXControllerConstantsLetters.X);
    JoystickButton lbButton = new JoystickButton(xboxController, XBOXControllerConstantsLetters.LB);
    JoystickButton rbButton = new JoystickButton(xboxController, XBOXControllerConstantsLetters.RB);
    // JoystickButton leftTrigger = new JoystickButton(xboxController, XBOXControllerConstantsLetters.left_triggerAxis);
    // JoystickButton rightTrigger = new JoystickButton(xboxController, XBOXControllerConstantsLetters.right_triggerAxis);
    
    aButton.onTrue(m_fuel.supportBotMacro());
    aButton.onFalse(m_fuel.stopCommand());

    xButton.onTrue(m_fuel.shootingCommand());
    xButton.onFalse(m_fuel.stopCommand());

    bButton.whileTrue(m_fuel.lebron2().withTimeout(0.5).andThen(m_fuel.lebron1()).finallyDo(() -> m_fuel.stopCommand()));
    bButton.whileFalse(m_fuel.stopCommand());

    lbButton.onTrue(m_fuel.groundIntakeCommand());
    lbButton.onFalse(m_fuel.stopCommand()); 

    rbButton.onTrue(m_fuel.groundOuttakeCommand());
    rbButton.onFalse(m_fuel.stopCommand());

    //yButton.onTrue(m_visionSubsystem.turnToTagTWOLEBRON(m_driveSubsystem));
    //yButton.onFalse(Command.runOnce(() -> m_driveSubsystem.stop()));

    //yButton.onTrue(m_visionSubsystem.driveToTagTWOLEBRON(m_driveSubsystem, 1.5));
    // yButton.onTrue(
    // m_visionSubsystem
    //     .driveToTagTWOLEBRON(m_driveSubsystem, 1.5)
    //     .andThen(() -> m_driveSubsystem.stop())
    // );
    //yButton.onFalse(m_driveSubsystem.stop());

    // rightTrigger.onTrue(m_fuel.fireProjectileAutonomousCommand(m_visionSubsystem.getDistance(), 72));
    // rightTrigger.onFalse(m_fuel.stopCommand());

  }

  private void configureBindings() {
  //this is for Logitech (THIS IS FOR BY TWICE!?!)
  
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


    lbButton.onTrue(m_fuel.groundIntakeCommand());
    rbButton.onTrue(m_fuel.groundOuttakeCommand());
    xButton.onTrue(m_fuel.shootingCommand());
    lbButton.onFalse(m_fuel.stopCommand());
    rbButton.onFalse(m_fuel.stopCommand());
    xButton.onFalse(m_fuel.stopCommand());

    bButton.whileTrue(m_fuel.lebron2().withTimeout(0.5).andThen(m_fuel.lebron1()).finallyDo(() -> m_fuel.stopCommand()));
    bButton.whileFalse(m_fuel.stopCommand());
    // Hold button to activate auto-lock
    // bButton.onTrue(m_visionSubsystem.turnToTarget());
  }
  
  private void configureDefaultCommands() {
  
    // Drivetrain Subsystem
    m_driveSubsystem.setDefaultCommand(
      Commands.run(
            () -> {
                double forward = -stick.getRawAxis(ControllerConstantsLetters.leftJoystickYAxis) * 0.5;
                double turn    = -stick.getRawAxis(ControllerConstantsLetters.rightJoystickXAxis) * 0.3;

                // If Xbox Controller is being used, override
                if (Math.abs(xboxController.getRawAxis(XBOXControllerConstantsLetters.leftJoystickYAxis)) > 0.1 ||
                    Math.abs(xboxController.getRawAxis(XBOXControllerConstantsLetters.rightJoystickXAxis)) > 0.1) {
                    forward = -xboxController.getRawAxis(XBOXControllerConstantsLetters.leftJoystickYAxis) * 0.8;
                    turn    = -xboxController.getRawAxis(XBOXControllerConstantsLetters.rightJoystickXAxis) * 0.5;
                }

                m_driveSubsystem.driveArcade(forward, -turn);
            },
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

    //  System.out.println("AUTO START");

      //return m_visionSubsystem.driveToTagLEBRON(m_driveSubsystem, 1.5);
      // return m_visionSubsystem.turnToTagTWOLEBRON(m_driveSubsystem).andThen(m_visionSubsystem.driveToTagTWOLEBRON(m_driveSubsystem, 1.5));

      return simpleAuto();

  }

    public Command simpleAuto() {
      return Commands.sequence(

          // Step 1: step back 1.5 meters (approx)
          new DriveCommand(m_driveSubsystem, () -> 0, () -> 1.2),

          // Step 2: shoot
          Commands.runOnce(() -> SmartDashboard.putBoolean("Shooting", true)),

          new FuelCommand(m_fuel, () -> 0, () -> 5),

          Commands.runOnce(() -> SmartDashboard.putBoolean("Shooting", false)),
          

          // Step 3: step forward
          new DriveCommand(m_driveSubsystem, () -> 1, () -> 1.2)
          

          // // Step 2: shoot
          // // m_fuel.autoShootingCommand(),
          // // new WaitCommand(5),


          // new DriveCommand(m_driveSubsystem, () -> 1, () -> 1.2).withTimeout(1.2),
          // new DriveCommand(m_driveSubsystem, () -> 2, () -> 1).withTimeout(1)

          // // Step 4: turn 30°
          // Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.4), m_driveSubsystem).withTimeout(0.5),
          // m_driveSubsystem.driveStopCommand()

          // // Step 5: go
          // Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          // m_driveSubsystem.driveStopCommand(),

          // // Step 6: turn
          // Commands.run(() -> m_driveSubsystem.driveArcade(0, -0.4), m_driveSubsystem).withTimeout(0.7),
          // m_driveSubsystem.driveStopCommand(),

          // // Step 7: go
          // Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          // m_driveSubsystem.driveStopCommand(),

          // // Step 8: intake
          // m_fuel.groundIntakeCommand(),
          // new WaitCommand(2),
          // m_fuel.stopCommand(),

          // // Step 9: turn 180
          // Commands.run(() -> m_driveSubsystem.driveArcade(0, 0.6), m_driveSubsystem).withTimeout(2),
          // m_driveSubsystem.driveStopCommand(),

          // // Step 10: go
          // Commands.run(() -> m_driveSubsystem.driveArcade(0.6, 0), m_driveSubsystem).withTimeout(2),
          // m_driveSubsystem.driveStopCommand()
      );
  }

  public Command simpleLEFTAuto() {
    return Commands.sequence(

        // Step back
        new DriveCommand(m_driveSubsystem, () -> 0, () -> 1.2)
            .withTimeout(1.5),

        // Stop after moving
        Commands.runOnce(() -> m_driveSubsystem.stop()),

        // Shoot
        new FuelCommand(m_fuel, () -> 0, () -> 5)
            .withTimeout(2),

        Commands.runOnce(() -> m_fuel.stopCommand()),

        // Turn ~ -30 degrees
        Commands.run(
            () -> m_driveSubsystem.driveArcade(0, -0.4),
            m_driveSubsystem
        ).withTimeout(0.5),

        Commands.runOnce(() -> m_driveSubsystem.stop())

        // // Drive to AprilTag
        // m_visionSubsystem
        //     .driveToTagTWOLEBRON(m_driveSubsystem, 1.5)
        //     .withTimeout(2.5),

        // Commands.runOnce(() -> m_driveSubsystem.stop())
    );
  }

  public Command simpleRIGHTAuto() {
    return Commands.sequence(

        // Step back
        new DriveCommand(m_driveSubsystem, () -> 0, () -> 1.2)
            .withTimeout(1.7),

        // Stop after moving
        Commands.runOnce(() -> m_driveSubsystem.stop()),

        // Shoot
        new FuelCommand(m_fuel, () -> 0, () -> 5)
            .withTimeout(2),

        Commands.runOnce(() -> m_fuel.stopCommand()),

        // Turn ~30 degrees
        Commands.run(
            () -> m_driveSubsystem.driveArcade(0, 0.4),
            m_driveSubsystem
        ).withTimeout(0.45),

        Commands.runOnce(() -> m_driveSubsystem.stop())

        // // Drive to AprilTag
        // m_visionSubsystem
        //     .driveToTagTWOLEBRON(m_driveSubsystem, 1.5)
        //     .withTimeout(2.5),

        // Commands.runOnce(() -> m_driveSubsystem.stop())
    );
  }
}
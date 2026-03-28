// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {

  public static final class DriveConstants {
    // Motor controller IDs for drivetrain motors
    public static final int LEFT_LEADER_ID = 1;
    public static final int LEFT_FOLLOWER_ID = 3;
    public static final int RIGHT_LEADER_ID = 2;
    public static final int RIGHT_FOLLOWER_ID = 4;

    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60; //max 30

    public static final int DRIVE_MOTOR_OVERHEAT_TEMPERATURE = 70; //degrees Celsius
  }

  public static final class FuelConstants {
    // Motor controller IDs for Fuel Mechanism motors
    public static final int LEFT_MOTOR_ID = 5;
    // Left motor is for both shooter fly wheel and intake rollers
    public static final int RIGHT_MOTOR_ID = 6;
    //Right motor is for pushing ball in or out

    //Note: the motors are on gear so be carfull of invering it 

    public static final int FUEL_MOTOR_CURRENT_LIMIT = 60;

    public static final int FUEL_MOTOR_OVERHEAT_TEMPERATURE = 70;

    // Voltage values for various fuel operations. These values may need to be tuned
    // based on exact robot construction.
    // See the Software Guide for tuning information
    public static final double INTAKING_FEEDER_VOLTAGE = -12;
    public static final double INTAKING_INTAKE_VOLTAGE = 10;
    public static final double LAUNCHING_FEEDER_VOLTAGE = 9;
    public static final double LAUNCHING_LAUNCHER_VOLTAGE = 10.6;
    public static final double SPIN_UP_FEEDER_VOLTAGE = -6;
    public static final double SPIN_UP_SECONDS = 1;

    public static final double fuelInSpeed = -0.4;
    public static final double fuelOutSpeed =0.4;
  }

  public static final class ShooterConstants {
    //public static final int SHOOTER_FUEL_MOTOR_ID = 7;

    public static final double shooterInSpeed = 0.4;
    public static final double shooterOutSpeed =-0.4;
  }

  public static final class ClimberConstants {
    //Climber Motor ID
    public static final int ClimberMotorID = 7;
    
    public static final double ClimberSpeedUp = 0.5;
    public static final double ClimberSpeedDown = -0.5;

    public static final double Climber_UP_VOLTAGE = 20;
    public static final double Climber_DOWN_VOLTAGE = 15;

    public static final int Climber_MOTOR_CURRENT_LIMIT = 60;
  }

  public static final class ControllerConstants {
    //INTAKE = L2 or LT
    public static final int INTAKE = 5;
    //OUTTAKE = R2 or RT
    public static final int SHOOTER = 6;
    //DynamicOutTake = R2 or RT
    public static final int DynamicOutTake = 67;
    //AUTOLOCK = LEFTARROW
    public static final int AUTOLOCK = 6;
    //GroundOUTTAKE = down arrow key
    public static final int GroundOUTTAKE = 7;
    //INFRONT SHOOTER HUB = A 
    public static final int HUBSHOOTER = 1;
    //CLIMB DOWN = X
    public static final int DOWN = 3;
    //CLIMB UP = Y
    public static final int UP = 4;
  }

    public static final class ControllerConstantsLetters {
      public static final int A = 1;
      public static final int B = 2;
      public static final int X = 3;
      public static final int Y = 4;
      public static final int LB = 5;
      public static final int RB = 6;
      public static final int back = 7;
      public static final int start = 8;
      public static final int leftJoystickPress = 9;
      public static final int rightJoystickPress = 10;

      public static final int leftJoystickXAxis = 0;
      public static final int leftJoystickYAxis = 1;
      public static final int rightJoystickXAxis = 4;
      public static final int rightJoystickYAxis = 5;
  }

  public static final class XBOXControllerConstantsLetters {
    
    public static final int A = 1; // Boost or Climb down
    public static final int B = 2; // G-outtake
    public static final int X = 3; //Shoot
    public static final int Y = 4; // Climb Up
    public static final int LB = 5; // intake
    public static final int RB = 6; // Climb Down

    //prob dont use
    public static final int back = 7;
    public static final int start = 8;
    public static final int leftJoystickPress = 9;
    public static final int rightJoystickPress = 10;

    //Axis
    public static final int leftJoystickXAxis = 0;
    public static final int leftJoystickYAxis = 1;
    public static final int rightJoystickXAxis = 4;
    public static final int rightJoystickYAxis = 5;

    public static final int left_triggerAxis = 2; // AUTO ALINE
    public static final int right_triggerAxis = 3; // Dynamic OutTake / LE BRON
  }

  public static final class OperatorConstants {
    // Port constants for driver and operator controllers. These should match the
    // values in the Joystick tab of the Driver Station software
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    // This value is multiplied by the joystick value when rotating the robot to
    // help avoid turning too fast and beign difficult to control
    public static final double DRIVE_SCALING = .7;
    public static final double ROTATION_SCALING = .8;
  }
}
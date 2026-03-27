package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.RelativeEncoder;

import static frc.robot.Constants.FuelConstants;

public class FuelSubsystem implements Subsystem {
    public static FuelSubsystem singleInst;
    public static FuelSubsystem getInst(){
        if (singleInst == null) singleInst = new FuelSubsystem();
        return singleInst;
    }

    private final SparkMax m_rightMotor;
    private final TalonFX m_leftMotor;
    private final SparkMaxConfig m_config_normal = new SparkMaxConfig();
    private final SparkMaxConfig m_config_inverted = new SparkMaxConfig();
    private final TalonFXConfiguration m_talon_config_normal = new TalonFXConfiguration();
    private final TalonFXConfiguration m_talon_config_inverted = new TalonFXConfiguration();

    // constructor
    public FuelSubsystem(){
        m_leftMotor = new TalonFX(FuelConstants.LEFT_MOTOR_ID);
        m_rightMotor = new SparkMax(FuelConstants.RIGHT_MOTOR_ID, MotorType.kBrushless); 
        
        m_talon_config_normal.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        m_talon_config_normal.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        m_talon_config_normal.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.1;

        m_talon_config_inverted.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        m_talon_config_inverted.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        m_talon_config_inverted.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.1;

        m_leftMotor.setSafetyEnabled(true);

        m_config_normal.inverted(false)
                       .smartCurrentLimit(FuelConstants.FUEL_MOTOR_CURRENT_LIMIT);
        m_config_inverted.inverted(true)
                         .smartCurrentLimit(FuelConstants.FUEL_MOTOR_CURRENT_LIMIT);

        
    }

    // methods
    public void setInvert(boolean status){
        if (status) {
                m_leftMotor.getConfigurator().apply(m_talon_config_inverted);
                m_rightMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        } else {
                m_leftMotor.getConfigurator().apply(m_talon_config_normal);
                m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        }
    }

    public void shoot(){
        m_leftMotor.getConfigurator().apply(m_talon_config_inverted);
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(20.0);
        Timer.delay(0.2);
        m_rightMotor.setVoltage(22.0);
    }
    
    public void autoShoot(){
        m_leftMotor.getConfigurator().apply(m_talon_config_inverted);
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(22.0);
        Timer.delay(0.2);
        m_rightMotor.setVoltage(22.0);
    }

    public void groundIntake(){
        setInvert(false);
        m_leftMotor.set(FuelConstants.fuelInSpeed);
        m_rightMotor.set(FuelConstants.fuelInSpeed);
    }

    public void groundOuttake(){
        setInvert(false);   
        m_leftMotor.set(FuelConstants.fuelOutSpeed);
        m_rightMotor.set(FuelConstants.fuelOutSpeed);
    }

    public void Stop(){
        m_leftMotor.set(0);
        m_rightMotor.set(0);
    }

    protected void isFinished() {
        Stop();
    }
    
    public void lebron() {
        m_leftMotor.setVoltage(8);
        m_rightMotor.setVoltage(12.6);
        Timer.delay(0.1);
        m_leftMotor.setVoltage(11);
        m_rightMotor.setVoltage(12.6);
    }

    public void lebronTWO() {
        m_leftMotor.setVoltage(11);
        m_rightMotor.setVoltage(12.6);
    }

    public void mid() {
        //this is for Being a mid-bot and taking fuel and immediately shooting to your side 
        m_leftMotor.getConfigurator().apply(m_talon_config_inverted);
        m_leftMotor.setVoltage(18);
        m_rightMotor.setVoltage(8);
    }
    
    // commands
    public Command groundIntakeCommand(){
        return runOnce(() -> {
            System.out.println("Ground Intake Command Ran");
            groundIntake();
        });
    }
    
    public Command groundOuttakeCommand(){
        return runOnce(() -> {
            System.out.println("Outtake Command Ran");
            groundOuttake();
        });
    }

    public Command shootingCommand(){
        return runOnce(() -> {
            System.out.println("Shooting Command Ran");
            shoot();
        });
    }
    
    public Command stopCommand(){
        return runOnce(() -> {
        System.out.println("Fuel System Stopped");
            Stop();
        });
    }

    public Command lebron1(){
        return this.run(() -> lebron());
    }
    public Command lebron2(){
        return this.run(() -> lebronTWO());
    }

    public Command supportBotMacro() {
        return this.run(() -> mid());
    }
}

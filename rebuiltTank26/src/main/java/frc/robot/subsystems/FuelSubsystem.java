package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import static frc.robot.Constants.FuelConstants;

public class FuelSubsystem implements Subsystem {
    public static FuelSubsystem singleInst;
    public static FuelSubsystem getInst(){
        if (singleInst == null) singleInst = new FuelSubsystem();
        return singleInst;
    }

    private final SparkMax m_leftMotor;
    private final SparkMax m_rightMotor;
    private final SparkMaxConfig m_config_normal = new SparkMaxConfig();
    private final SparkMaxConfig m_config_inverted = new SparkMaxConfig();

    // constructor
    public FuelSubsystem(){
        m_leftMotor = new SparkMax(FuelConstants.LEFT_MOTOR_ID, MotorType.kBrushed);
        m_rightMotor = new SparkMax(FuelConstants.RIGHT_MOTOR_ID, MotorType.kBrushed); 
        m_config_normal.inverted(false);
        m_config_inverted.inverted(true);
    }

    // methods
    public void setInvert(boolean status){
        if (status) {
                m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
                m_rightMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        } else {
                m_leftMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
                m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        }
    }

    public void shoot(){
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(20.0);
        Timer.delay(1);
        m_rightMotor.setVoltage(20.0);
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
}

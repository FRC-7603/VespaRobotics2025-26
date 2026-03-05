package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.spark.SparkMax;
import static frc.robot.Constants.FuelConstants;

public class FuelSubsystem implements Subsystem {
    public static FuelSubsystem singleInst;
    public static FuelSubsystem getInst(){
        if (singleInst == null) singleInst = new FuelSubsystem();
        return singleInst;
    }

    private final SparkMax m_leftMotor;
    private final SparkMax m_rightMotor;


    // constructor
    public FuelSubsystem(){
        m_leftMotor = new SparkMax(FuelConstants.LEFT_MOTOR_ID, MotorType.kBrushed);
        m_rightMotor = new SparkMax(FuelConstants.RIGHT_MOTOR_ID, MotorType.kBrushed); 
    }

    // methods
    public void setInvert(boolean status){
        m_leftMotor.setInverted(status);
        m_rightMotor.setInverted(status);
    }

    public void shoot(){
        m_leftMotor.setInverted(true);
        m_rightMotor.setInverted(false);
        m_leftMotor.setVoltage(15.0);
        m_rightMotor.setVoltage(20.0);
    }

    public void groundIntake(){
        setInvert(false);
        m_leftMotor.set(FuelConstants.fuelInSpeed);
        m_rightMotor.set(FuelConstants.fuelOutSpeed);
    }

    public void groundOuttake(){
        setInvert(false);   
        m_leftMotor.set(FuelConstants.fuelOutSpeed);
        m_rightMotor.set(FuelConstants.fuelInSpeed);
    }

    public void Stop(){
        m_leftMotor.set(0);
        m_rightMotor.set(0);
    }
    
    // commands
    public Command groundIntakeCommand(){
        return run(() -> {
            System.out.println("Ground Intake Command Ran");
            groundIntake();
        });
    }
    
    public Command groundOuttakeCommand(){
        return run(() -> {
            System.out.println("Outtake Command Ran");
            groundOuttake();
        });
    }

    public Command shootingCommand(){
        return run(() -> {
            System.out.println("Shooting Command Ran");
            shoot();
        });
    }
    
    public Command stopCommand(){
        return run(() -> {
            System.out.println("Intake stopped");
            Stop();
        });
    }
}

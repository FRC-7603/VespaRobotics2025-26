package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.spark.SparkMax;
import static frc.robot.Constants.FuelConstants;

public class Intake implements Subsystem {
    public static Intake singleInst;
    public static Intake getInst(){
        if (singleInst == null) singleInst = new Intake();
        return singleInst;
    }

    private final SparkMax m_intakeMotor;
    private final SparkMax m_outtakeMotor;


    // constructor
    public Intake(){
        m_intakeMotor = new SparkMax(FuelConstants.INTAKE_MOTOR_ID, MotorType.kBrushed);
        m_outtakeMotor = new SparkMax(FuelConstants.OUTTAKE_MOTOR_ID, MotorType.kBrushed); 
    }

    // methods
    public void setSpeed(double speed) {
        m_intakeMotor.set(speed);
        m_outtakeMotor.set(speed);
    }

    public void intake(){
        setSpeed(FuelConstants.fuelInSpeed);
    }
    public void unstuck(){
        setSpeed(FuelConstants.fuelOutSpeed);
    }
    public void Stop(){
        setSpeed(0);
    }
    

    // commands
    public Command intakeCommand(){
        return run(() -> {
            System.out.println("Intake Command Ran");
            intake();
        });
    }
    
    public Command groundOuttakeCommand(){
        return run(() -> {
            System.out.println("Outtake Command Ran");
            unstuck();
        });
    }
    
    public Command stopCommand(){
        return run(() -> Stop());
    }
}

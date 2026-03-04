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

    private final SparkMax m_fuelMotorTwo;


    // constructor
    public Intake(){
        m_fuelMotorTwo = new SparkMax(FuelConstants.FUEL_MOTOR_ID, MotorType.kBrushed); 
    }

    
    // methods
    public void intake(){
        m_fuelMotorTwo.set(FuelConstants.fuelInSpeed);
    }
    public void unstuck(){
        m_fuelMotorTwo.set(FuelConstants.fuelOutSpeed);
    }
    public void Stop(){
        m_fuelMotorTwo.set(0);
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

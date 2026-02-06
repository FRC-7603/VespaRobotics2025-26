package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RevMotor;
import static frc.robot.Constants.FuelConstants;

public class Intake implements Subsystem{
    public static Intake singleInst;
    public static Intake getInst(){
        if (singleInst == null) singleInst = new Intake();
        return singleInst;
    }

    RevMotor fuelMotorTWO;

    double fuelInSpeed = 0.4;
    double fuelOutSpeed =-0.4;

    public Intake(){
        
        fuelMotorTWO = new RevMotor(FuelConstants.INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushed); 
    }
    
    public void FuelIn(){
        fuelMotorTWO.Motor.set(fuelInSpeed);
    }
    public void FuelOut(){
        fuelMotorTWO.Motor.set(fuelOutSpeed);
    }
    public void Stop(){
        fuelMotorTWO.Motor.set(0);;
    }
    
    public Command FuelInCommand(){
        return run(()->{
            System.out.println("Fuel In");
            FuelIn();
        });
    }
    
    public Command FuelOutCommand(){
        return run(this::FuelOut);
    }
    
    public Command FuelStopCommand(){
        return run(this::Stop);
    }
}

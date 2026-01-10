package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RevMotor;

public class Shooter implements Subsystem {

    public static Shooter singleInst;
    public static Shooter getInst(){
        if (singleInst == null) singleInst = new Shooter();
        return singleInst;
    }

    RevMotor fuelMotor;

    double fuelInSpeed = 0.4;
    double fuelOutSpeed =-0.4;

    public Shooter(){
        
        fuelMotor = new RevMotor(4, MotorType.kBrushless);
    }
    
    public void FuelIn(){
        fuelMotor.Motor.set(fuelInSpeed);
    }
    public void FuelOut(){
        fuelMotor.Motor.set(fuelOutSpeed);
    }
    public void Stop(){
        fuelMotor.Motor.set(0);;
    }
    
    @Override
    public void periodic(){
        //fuelMotor.resetReference();
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

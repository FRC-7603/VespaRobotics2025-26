package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RevMotor;
import static frc.robot.Constants.FuelConstants;

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
        // set the deviceID for Rev 
        fuelMotor = new RevMotor(FuelConstants.SHOOTER_FUEL_MOTOR_ID, MotorType.kBrushed);
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

    public Command fireProjectileCommand(double yaw, double range){
        double gravity = 9.81;
        double wheelCircumfrenceINCHES = 4.724409;
        double targetVelocity = Math.sqrt((range*gravity)/Math.sin(2*yaw));
        double targetRPM = (targetVelocity*60*12)/wheelCircumfrenceINCHES;
        double targetRPMAfterGearRatio = targetRPM/1.04;

        return run(() -> {
            fuelMotor.setRefernce(targetRPMAfterGearRatio, ControlType.kVelocity);
        });
    }
    
}

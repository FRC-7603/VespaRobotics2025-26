package frc.robot.subsystems;

//import com.revrobotics.spark.SparkMax;
//import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
//import com.revrobotics.spark.config.SparkMaxConfig;
//import com.revrobotics.spark.SparkBase.PersistMode;
//import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.ClimberMotorsID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RevMotor;

public class Climber implements Subsystem {

    public static Climber singleInst;
    public static Climber getInst(){
        if (singleInst == null) singleInst = new Climber();
        return singleInst;
    }

    double ClimberSpeedUp = 0.4;
    double ClimberSpeedDown = -0.4;

    RevMotor fuelMotorTWO;

    public Climber(){
        // set the deviceID for Rev 
        fuelMotorTWO = new RevMotor(ClimberMotorsID.Climber_Motor_ID, MotorType.kBrushless); 
        // idk if brushless yet
    }
    
    public void ClimberSpeedUp(){
        fuelMotorTWO.Motor.set(ClimberSpeedUp);
    }
    public void ClimberSpeedDown(){
        fuelMotorTWO.Motor.set(ClimberSpeedDown);
    }
    public void Stop(){
        fuelMotorTWO.Motor.set(0);;
    }
    //Set of Commands used for the CLimber System
    public Command ClimberSpeedDownCommand(){
        return run(()->{
            System.out.println("Fuel In");
            ClimberSpeedDown();
        });
    }
    
    public Command ClimberSpeedUpCommand(){
        return run(this::ClimberSpeedUp);
    }
    
    public Command FuelStopCommand(){
        return run(this::Stop);
    }
}


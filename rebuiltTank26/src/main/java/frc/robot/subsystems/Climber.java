package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.spark.SparkMax;
import static frc.robot.Constants.ClimberConstants;

public class Climber implements Subsystem {
    public static Climber singleInst;
    public static Climber getInst(){
        if (singleInst == null) singleInst = new Climber();
        return singleInst;
    }

    private final SparkMax m_climberMotor;


    // constructor
    public Climber(){
        m_climberMotor = new SparkMax(ClimberConstants.ClimberMotorID, MotorType.kBrushless); 
    }
    

    // methods
    public void speedUp(){
        m_climberMotor.set(ClimberConstants.ClimberSpeedUp);
    }
    public void speedDown(){
        m_climberMotor.set(ClimberConstants.ClimberSpeedDown);
    }
    public void Stop(){
    m_climberMotor.set(0);
    }

    // commands
    public Command ClimberSpeedDownCommand(){
        return run(() -> {
            System.out.println("Climber In");
            speedDown();
        });
    }
    
    public Command ClimberSpeedUpCommand(){
        return run(() -> {
            System.out.println("Climber Out");
            speedUp();
        });
    }
    
    public Command ClimberStopCommand(){
        return run(() -> Stop());
    }
}


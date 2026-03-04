package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.spark.SparkMax;
import static frc.robot.Constants.ShooterConstants;

public class Shooter implements Subsystem {
    public static Shooter singleInst;
    public static Shooter getInst(){
        if (singleInst == null) singleInst = new Shooter();
        return singleInst;
    }

    private final SparkMax m_fuelMotor;


    // constructor
    public Shooter(){
        m_fuelMotor = new SparkMax(ShooterConstants.SHOOTER_FUEL_MOTOR_ID, MotorType.kBrushed);
    }
    

    // methods
    public void ShooterShoot(){
        m_fuelMotor.set(ShooterConstants.shooterInSpeed);
    }

    public void ShooterUnstick(){
       m_fuelMotor.set(ShooterConstants.shooterOutSpeed);
    }

    public void Stop(){
        m_fuelMotor.set(0);
    }


    // commands
    public Command ShooterShootCommand(){
        return run(() -> {
            System.out.println("Fuel In");
            ShooterShoot();
        });
    }
    
    public Command ShooterUnstickCommand(){
        return run(() -> {
            System.out.println("Fuel Out");
            ShooterUnstick();
        });
    }
    
    public Command ShooterStopCommand(){
        return run(() -> Stop());
    }
}

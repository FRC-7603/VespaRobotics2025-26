package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.FuelConstants;
import frc.robot.RevMotor;
import static frc.robot.Constants.FuelConstants;

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
    
    public Command FuelStopCommand(){
        return run(this::Stop);
    }   

    public Command fireProjectileCommand(double range, double heightTarget){
        double gravity = 9.81;
        double wheelCircumfrence = 0.11999999;
        double heightLeBron = 0.4953;
        double thota = 80; // this is in degrees to be converted to radians later
        range +=  0.4318; //this accounts for the distance from the robots projectile storage to the fron to f the robot
        double cosSquared = Math.cos(Math.toRadians(thota))*Math.cos(Math.toRadians(thota));
        double constantThing = (25*60/26*Math.PI*wheelCircumfrence) * (Math.sqrt(gravity/2*cosSquared));
        double targetRpm = constantThing*(range/
        (Math.sqrt(range*
        Math.tan(Math.toRadians(range))
        -(heightTarget - heightLeBron))))*1.4; // the 1.4 is because i assume its a closed system so to account for losses
        return run(() -> {
            fuelMotor.setRefernce(targetRpm, ControlType.kVelocity);
        });
    }
    
}

package frc.robot.subsystems;
// le bron shoot fr fr

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

import static frc.robot.Constants.FuelConstants;

public class FuelSubsystem implements Subsystem {
    public static FuelSubsystem singleInst;
    public static FuelSubsystem getInst(){
        if (singleInst == null) singleInst = new FuelSubsystem();
        return singleInst;
    }

    private final SparkMax m_leftMotor;
    private final SparkMax m_rightMotor;
    private final SparkMaxConfig m_config_normal = new SparkMaxConfig();
    private final SparkMaxConfig m_config_inverted = new SparkMaxConfig();
    private final RelativeEncoder m_leftEncoder;
    private final SparkClosedLoopController m_leftPID;
    private final Alert m_overheatAlert = new Alert("Fuel motors are overheating!", AlertType.kWarning);

    // constructor
    public FuelSubsystem(){
        m_leftMotor = new SparkMax(FuelConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
        m_rightMotor = new SparkMax(FuelConstants.RIGHT_MOTOR_ID, MotorType.kBrushless); 
        m_config_normal.inverted(false)
                       .smartCurrentLimit(FuelConstants.FUEL_MOTOR_CURRENT_LIMIT);
        m_config_inverted.inverted(true)
                         .smartCurrentLimit(FuelConstants.FUEL_MOTOR_CURRENT_LIMIT);
        m_leftEncoder = m_leftMotor.getEncoder();
        m_leftPID = m_leftMotor.getClosedLoopController();
    }

    // methods
    public void setInvert(boolean status){
        if (status) {
                m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
                m_rightMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        } else {
                m_leftMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
                m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        }
    }

    public void shoot(){
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(20);
        m_rightMotor.setVoltage(22.0);
    }
    
    public void preheat(){
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(12);

    }

    public void autoShoot(){
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(12);
        m_rightMotor.setVoltage(22.0);
    }

    public void groundIntake(){
        setInvert(false);
        m_leftMotor.set(FuelConstants.fuelInSpeed);
        m_rightMotor.set(FuelConstants.fuelInSpeed);
    }

    public void groundOuttake(){
        setInvert(false);   
        m_leftMotor.set(FuelConstants.fuelOutSpeed);
        m_rightMotor.set(FuelConstants.fuelOutSpeed);
    }

    public void Stop(){
        m_leftMotor.set(0);
        m_rightMotor.set(0);
    }

    @Override
    public void periodic() {
        m_overheatAlert.set(
            m_leftMotor.getMotorTemperature() > FuelConstants.FUEL_MOTOR_OVERHEAT_TEMPERATURE ||
            m_rightMotor.getMotorTemperature() > FuelConstants.FUEL_MOTOR_OVERHEAT_TEMPERATURE
        );
    }
    
    public void lebron() {
        m_leftMotor.setVoltage(-8);
        m_rightMotor.setVoltage(12.6);
        //Timer.delay(0.234);
        m_leftMotor.setVoltage(11);
        m_rightMotor.setVoltage(12.6);
    }

    public void lebronTWO() {
        m_leftMotor.setVoltage(11);
        m_rightMotor.setVoltage(12.6);
    }

    public void mid() {
        //this is for Being a mid-bot and taking fuel and immediately shooting to your side 
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(20);
        m_rightMotor.setVoltage(8); //8 -> 6
    }
    
    // commands
    public Command groundIntakeCommand(){
        return runOnce(() -> {
            System.out.println("Ground Intake Command Ran");
            groundIntake();
        });
    }
    
    public Command groundOuttakeCommand(){
        return runOnce(() -> {
            System.out.println("Outtake Command Ran");
            groundOuttake();
        });
    }

    public Command shootingCommand(){
        return runOnce(() -> {
            System.out.println("Shooting Command Ran");
            shoot();
        });
    }
    
    public Command stopCommand(){
        return runOnce(() -> {
        System.out.println("Fuel System Stopped");
            Stop();
        });
    }

    public Command lebron1(){
        return this.run(() -> lebron());
    }
    public Command lebron2(){
        return this.run(() -> lebronTWO());
    }

    public Command supportBotMacro() {
        return this.run(() -> mid());
    }

    public Command fireProjectileAutonomousCommand(double range, double heightTarget){
        double gravity = 9.81;
        double wheelCircumfrence = 0.11999999;
        double heightLeBron = 0.4953;
        double thota = 80; // this is in degrees to be converted to radians later
        range +=  0.4318; //this accounts for the distance from the robots projectile storage to the fron to f the robot
        double cosSquared = Math.cos(Math.toRadians(thota))*Math.cos(Math.toRadians(thota));
        double constantThing = (25*60/26*Math.PI*wheelCircumfrence) * (Math.sqrt(gravity/2*cosSquared));
        double targetRpm = constantThing*(range/
        (Math.sqrt(range*
        Math.tan(Math.toRadians(thota))
        -(heightTarget - heightLeBron))))*1.4; // the 1.4 is because i assume its a closed system so to account for losses
        return run(() -> {
            m_leftPID.setSetpoint(targetRpm, ControlType.kVelocity);
        });
    }
}
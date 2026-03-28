package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.RelativeEncoder;

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


    //Telemetry - update shooter state every loop
    // THIS IS PERIODIC BY TWICE????? 
    @Override
    public void periodic() {
        double rpm = getShooterRPM();
        SmartDashboard.putNumber("Shooter RPM", rpm);
        SmartDashboard.putBoolean("Shooter Ready", isShooterAtSpeed());
        SmartDashboard.putNumber("Shooter Target RPM", FuelConstants.SHOOTER_TARGET_RPM);
    }


    //return the current shooter (left motor) velocity in RPM.
    // THIS IS RPM BY TWICE?????
    public double getShooterRPM() {
        return Math.abs(m_leftEncoder.getVelocity());
    }

    // return true when the shooter wheel is within the tolerance band of the target RPM and is therefore safe to feed a ball.
    // THIS IS SHOOTER READY BY TWICE?????
    public boolean isShooterAtSpeed() {
        double tolerance = FuelConstants.SHOOTER_TARGET_RPM
                * (FuelConstants.SHOOTER_RPM_TOLERANCE_PERCENT / 100.0);
        return getShooterRPM() >= (FuelConstants.SHOOTER_TARGET_RPM - tolerance);
    }

    /* 
    Spin up the shooter wheel (and the bar/rollers on the same shaft)
    WITHOUT feeding the ball. The bar will be spinning at full speed
    so it can grab and lift the ball the moment the feeder pushes it in.
    */
    public void spoolShooter() {
        m_leftMotor.configure(m_config_inverted, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_leftMotor.setVoltage(FuelConstants.SHOOTER_SPOOL_VOLTAGE);
    }

    /*
    Push the ball out of storage and into the bar/rollers.
    Uses a moderate voltage so the ball is eased into the spinning
    bar rather than slammed this reduces the compression force
    against the rails and prevents jams.
    */
    public void runFeeder() {
        m_rightMotor.configure(m_config_normal, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_rightMotor.setVoltage(FuelConstants.FEEDER_FIRE_VOLTAGE);
    }

    // Stop only the feeder motor (shooter keeps spinning).
    public void stopFeeder() {
        m_rightMotor.set(0);
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

    /*
    Legacy shoot – now just an alias that spools the shooter.
    */
    public void shoot(){
        spoolShooter();
    }
    
    /*
    Legacy autoShoot – spools the shooter only.
    */
    public void autoShoot(){
        spoolShooter();
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

    protected void isFinished() {
        Stop();
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
        m_rightMotor.setVoltage(8);
    }
    
    //Commands

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

    /*
    The bar and shooter wheel share the LEFT motor.  The ball must NOT
    reach the bar until the wheel/bar is at full RPM, otherwise the
    compression against the rails will stall the motor and jam.
    
    Phase 1 – Spool:  Spins the left motor (shooter wheel + bar) up to
                        target RPM.  Feeder is OFF so the ball stays in
                        storage.
    Phase 2 – Feed:   Once RPM is within tolerance, the right motor
                        (feeder) gently pushes the ball into the spinning
                        bar which lifts it into the rails and fires it.
    Phase 3 – Guard:  If the RPM drops below threshold (ball entering
                        the rails absorbs energy), the feeder instantly
                        stops to avoid forcing more balls into the jam
                        zone.  It resumes only when RPM recovers.
    Phase 4 – End:    Button released → both motors stop.
    */
    public Command shootWhenReadyCommand() {
        return Commands.run(() -> {
            // Always keep the shooter wheel + bar spinning
            spoolShooter();

            if (isShooterAtSpeed()) {
                // Wheel & bar are at speed → safe to push the ball into the bar
                runFeeder();
                SmartDashboard.putBoolean("Feeding Ball", true);
            } else {
                // Still spooling, or RPM dropped because a ball is being
                // compressed against the rails → hold the feeder to avoid
                // forcing another ball into the jam zone.
                stopFeeder();
                SmartDashboard.putBoolean("Feeding Ball", false);
            }
        }, this)
        .finallyDo(interrupted -> {
            Stop();
            SmartDashboard.putBoolean("Feeding Ball", false);
            System.out.println("ShootWhenReady ended" + (interrupted ? " (interrupted)" : ""));
        });
    }

    /*
    Old shoot command for backward compatibility.
    */
    public Command shootingCommand(){
        return shootWhenReadyCommand();
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

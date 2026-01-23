package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem implements Subsystem{

    private final SparkMax feederRoller;
    private final SparkMax intakeLauncherRoller;

    public CANFuelSubsystem() {

        double fuelInSpeed = 0.4;
        double fuelOutSpeed =-0.4;

        intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushed);
        feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushed);

        SparkMaxConfig feederConfig = new SparkMaxConfig();
        feederConfig.smartCurrentLimit(FEEDER_MOTOR_CURRENT_LIMIT);
        feederRoller.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig launcherConfig = new SparkMaxConfig();
        launcherConfig.inverted(true);
        launcherConfig.smartCurrentLimit(LAUNCHER_MOTOR_CURRENT_LIMIT);
        intakeLauncherRoller.configure(launcherConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    }

    // A method to set the voltage of the intake roller
    public void setIntakeLauncherRoller(double voltage) {
        intakeLauncherRoller.setVoltage(voltage);
    }

    // A method to set the voltage of the intake roller
    public void setFeederRoller(double voltage) {
        feederRoller.setVoltage(voltage);
    }

    // A method to stop the rollers
    public void stop() {
        feederRoller.set(0);
        intakeLauncherRoller.set(0);
    }

    @Override
    public void periodic() {
    // This method will be called once per scheduler run
    }
}


package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AutoLockAprilTag extends SubsystemBase {

    private final CANDriveSubsystem drive;
    private final VisionSubsystem vision;

    private boolean active = false;

    private static final double kP = 0.02;
    private static final double maxTurn = 0.4;

    public AutoLockAprilTag(CANDriveSubsystem drive, VisionSubsystem vision) {
        this.drive = drive;
        this.vision = vision;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
        drive.stop();
    }

    @Override
    public void periodic() {
        if (!active) return;

        if (!vision.hasTarget()) {
            drive.stop();
            return;
        }

        double yaw = vision.getYaw();
        double turn = yaw * kP;
        turn = Math.max(-maxTurn, Math.min(maxTurn, turn));

        drive.driveArcade(0.0, turn);
    }
}
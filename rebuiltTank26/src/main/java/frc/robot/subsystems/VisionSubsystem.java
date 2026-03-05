package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSubsystem extends SubsystemBase {

    private final PhotonCamera camera;

    public VisionSubsystem() {
        camera = new PhotonCamera("photonvision"); // must match PhotonVision UI name
    }

    public boolean hasTarget() {
        return camera.getLatestResult().hasTargets();
    }

    public PhotonTrackedTarget getBestTarget() {
        if (!hasTarget()) return null;
        return camera.getLatestResult().getBestTarget();
    }

    public double getYaw() {
        PhotonTrackedTarget target = getBestTarget();
        return (target == null) ? 0.0 : target.getYaw();
    }


    // Command to turn robot toward the target

    public Command turnToTarget(CANDriveSubsystem drive) {
        return run(() -> {
            if (!hasTarget()) {
                drive.stop();
                return;
            }

            double yaw = getYaw();

            // simple pid turn
            double kP = 0.02;
            double turn = yaw * kP;

            drive.driveArcade(0, turn);
        });
    }
}

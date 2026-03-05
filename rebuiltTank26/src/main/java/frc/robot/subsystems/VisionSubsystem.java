package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.DriveSubsystem;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.PhotonUtils;

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

    public double getDistance() {
        PhotonTrackedTarget target = getBestTarget();
        if (target == null) return 0.0;

        double cameraHeight = 0.5; // meters (height of camera from floor)
        double targetHeight = 1.5; // meters (height of tag/target center)
        double cameraPitch = Math.toRadians(20); // camera tilt angle
        double targetPitch = Math.toRadians(target.getPitch());

        return PhotonUtils.calculateDistanceToTargetMeters(
            cameraHeight,
            targetHeight,
            cameraPitch,
            targetPitch
        );
    }

    public Command holdDistance(DriveSubsystem drive) {
        return run(() -> {
            if (!hasTarget()) {
                drive.stop();
                return;
            }

        PhotonTrackedTarget target = getBestTarget();

        double yaw = target.getYaw();
        double area = target.getArea();

        // tuning constants
        double turnKP = 0.02;
        double forwardKP = 0.1;

        double desiredDistance = 10; // target size when at desired distance

        double turn = yaw * turnKP;
        double forward = (desiredDistance - area) * forwardKP;

        drive.driveArcade(forward, turn);
        
        });
    }

    // Command to turn robot toward the target

    public Command turnToTarget(DriveSubsystem drive) {
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

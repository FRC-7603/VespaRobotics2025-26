package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.DriveSubsystem;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.PhotonUtils;

public class VisionSubsystem extends SubsystemBase {

    private final PhotonCamera camera;

    private Integer lockedTagID = null;
    

    public VisionSubsystem() {
        camera = new PhotonCamera("photonvision"); // must match PhotonVision UI name
    }

    public boolean hasTarget() {
        return camera.getLatestResult().hasTargets();
    }

    // public int getID() {
    //     PhotonTrackedTarget target = getBestTarget();
    //     return target.getFiducialId();
    // }

    // public PhotonTrackedTarget getBestTarget() {
    //     if (!hasTarget()) return null;
    //     return camera.getLatestResult().getBestTarget();
    // }

    public PhotonTrackedTarget getLockedTarget() {
    var result = camera.getLatestResult();

    if (!result.hasTargets()) {
        return null;
    }

    // If we already have a locked ID, find that tag
    if (lockedTagID != null) {
        for (PhotonTrackedTarget t : result.getTargets()) {
            if (t.getFiducialId() == lockedTagID) {
                return t;
            }
        }

        // Locked tag not visible
        return null;
    }

    // No tag locked yet → pick best
    PhotonTrackedTarget best = result.getBestTarget();
    lockedTagID = best.getFiducialId();
    return best;
}

    public void setTargetTag(int id) {
        lockedTagID = id;
    }

    public double getYaw() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0;
        return target.getYaw();
    }

    public double getDistance() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0;

        double cameraHeight = 0.5;
        double targetHeight = 1.5;
        double cameraPitch = Math.toRadians(20);
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

            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) {
                drive.stop();
                return;
            }

            double yaw = target.getYaw();
            double distance = getDistance();

            double turnKP = 0.02;
            double forwardKP = 0.6;

            double desiredDistance = 1.5; // meters

            double turn = yaw * turnKP;
            double forward = (desiredDistance - distance) * forwardKP;

            drive.driveArcade(forward, turn);

        });
    }

    // Command to turn robot toward the target
    public Command turnToTarget(DriveSubsystem drive) {
        return run(() -> {

            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) {
                drive.stop();
                return;
            }

            double yaw = target.getYaw();

            double kP = 0.02;
            double turn = yaw * kP;

            drive.driveArcade(0, turn);

        }).until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            return target != null && Math.abs(target.getYaw()) < 1.5;
        });
    }
}
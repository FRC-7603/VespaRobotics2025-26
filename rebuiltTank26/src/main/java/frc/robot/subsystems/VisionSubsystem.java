package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.DriveSubsystem;

import org.photonvision.targeting.PhotonPipelineResult;

import java.util.concurrent.Delayed;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.PhotonUtils;

public class VisionSubsystem extends SubsystemBase {

    private final PhotonCamera camera;
    private Integer lockedTagID = 7;

    // Cache the latest result each loop
    private PhotonPipelineResult latestResult = new PhotonPipelineResult();
    
    public VisionSubsystem() {
        camera = new PhotonCamera("cam");
    }

    @Override
    public void periodic() {
        var unread = camera.getAllUnreadResults();
        if (!unread.isEmpty()) {
            latestResult = unread.get(unread.size() - 1); // grab newest frame
        }
    }

    public void resetLockedTag() {
        lockedTagID = null;
    }

    public void setTargetTag(int id) {
        lockedTagID = id;
        //ex vision.setTargetTag(7);
    }

    public boolean hasTarget() {
        return camera.getLatestResult().hasTargets();
    }

    // public int getID() {
    //     PhotonTrackedTarget target = getBestTarget();
    //     return target.getFiducialId();
    // }

    public PhotonTrackedTarget getBestTarget() {
        if (!hasTarget()) return null;
        return camera.getLatestResult().getBestTarget();
    }

    public PhotonTrackedTarget getLockedTarget() {
        if (!latestResult.hasTargets()) return null;

        if (lockedTagID != null) {
            for (PhotonTrackedTarget tag : latestResult.getTargets()) {
                if (tag.getFiducialId() == lockedTagID) {
                    return tag;
                }
            }
            return null; // locked tag not visible
        }

        // Lock onto best visible tag
        PhotonTrackedTarget best = latestResult.getBestTarget();
        lockedTagID = best.getFiducialId();
        return best;
    }

    public double getYaw() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0;
        return target.getYaw();
    }

    public double getDistance() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0;

        final double cameraHeight = 0.5;
        final double targetHeight = 1.5;
        final double cameraPitch  = Math.toRadians(20);
        double targetPitch  = Math.toRadians(target.getPitch());

        return PhotonUtils.calculateDistanceToTargetMeters(
            cameraHeight, targetHeight, cameraPitch, targetPitch);
    }

    public Command holdDistance(DriveSubsystem drive) {
        return run(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) { drive.stop(); return; }

            double turn = -target.getYaw() * 0.02;
            double forward = (getDistance() - 1.5) * 0.6;
            drive.driveArcade(forward, turn);
        });
    }

    public Command turnToTarget(DriveSubsystem drive) {
        return run(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) { drive.stop(); return; }
            drive.driveArcade(0, -target.getYaw() * 0.02);
        }).until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            return target != null && Math.abs(target.getYaw()) < 1.5;
        });
    }

    public Command driveToTag(DriveSubsystem drive, double targetDistanceMeters) {
        return run(() -> {

            PhotonTrackedTarget target = getLockedTarget();

            if (target == null) {
                System.out.println("null no target");
                drive.stop();
                return;
            }

            double yaw = target.getYaw();
            double distance = getDistance();

            // Turn control (Y alignment)
            double turn = yaw * 4;

            // Forward control (X distance)
            double forward = (distance - targetDistanceMeters) * 4;

            
            drive.driveArcade(forward, turn);
            System.out.println("turn:" + turn);
            System.out.println("forward:" + forward);

        }).until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) return false;
            System.out.println("also null target");

            return Math.abs(getDistance() - targetDistanceMeters) < 0.1
                && Math.abs(target.getYaw()) < 1;
        });
    }

    public Command driveToTagLEBRON(DriveSubsystem drive, double targetDistanceMeters) {

    return run(() -> {

        PhotonTrackedTarget target = getLockedTarget();

        if (target == null) {
            // No tag detected — stop but keep trying next loop
            drive.stop();
            System.out.println("No target detected");
            return;
        }

        double yaw = target.getYaw();        // left/right angle
        double distance = getDistance();     // distance from tag

        // Calculate errors
        double yawError = yaw;
        double distanceError = distance - targetDistanceMeters;

        // Control gains (tune these)
        double turn = yawError * 0.02;
        double forward = distanceError * 0.6;

        // Clamp speeds so robot moves smoothly
        turn = Math.max(-0.4, Math.min(0.4, turn));
        forward = Math.max(-0.5, Math.min(0.5, forward));

        drive.driveArcade(forward, turn);

        // Debug prints
        System.out.println("Distance: " + distance);
        System.out.println("Yaw: " + yaw);
        System.out.println("Forward: " + forward);
        System.out.println("Turn: " + turn);

    })

    // Stop when close enough
    .until(() -> {

        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return false;

        double distanceError = Math.abs(getDistance() - targetDistanceMeters);
        double yawError = Math.abs(target.getYaw());

        return distanceError < 0.35 && yawError < 5;

    })

    // Stop robot when command ends
    .finallyDo((interrupted) -> drive.stop());
}

}
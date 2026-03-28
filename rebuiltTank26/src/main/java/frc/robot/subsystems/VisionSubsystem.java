package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.DriveSubsystem;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.PhotonUtils;

public class VisionSubsystem extends SubsystemBase {

    private final PhotonCamera camera;

    // Cache the latest result each loop
    private PhotonPipelineResult latestResult = new PhotonPipelineResult();

    public VisionSubsystem() {
        camera = new PhotonCamera("cam");
    }

    @Override
    public void periodic() {
        // call getAllUnreadResults exactly once per loop
        var unread = camera.getAllUnreadResults(); //
        if (!unread.isEmpty()) {
            latestResult = unread.get(unread.size() - 1); // newest frame
        }
    }

    // True if the latest cached result has any targets.
    public boolean hasTarget() {
        return latestResult.hasTargets();
    }

    // Best target from the latest cached result, or null if none.
    public PhotonTrackedTarget getBestTarget() {
        if (!hasTarget()) return null;
        return latestResult.getBestTarget();
    }

    private PhotonTrackedTarget getLockedTarget() {
        return getBestTarget();
    }

    public double getYaw() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0.0;
        return target.getYaw(); // degrees, left positive
    }

    public double getDistance() {
        PhotonTrackedTarget target = getLockedTarget();
        if (target == null) return 0.0;

        // replace with real constants (meters, radians)
        final double cameraHeight = 0.5;      // meters
        final double targetHeight = 1.5;      // meters
        final double cameraPitch  = Math.toRadians(20.0); // radians

        double targetPitch = Math.toRadians(target.getPitch()); // degrees -> radians

        return PhotonUtils.calculateDistanceToTargetMeters(
                cameraHeight, targetHeight, cameraPitch, targetPitch); // [web:4]
    }

    // probably too twitchy, but compiles
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

            double turn = yaw * 4.0;
            double forward = (distance - targetDistanceMeters) * 4.0;

            drive.driveArcade(forward, turn);
            System.out.println("turn:" + turn);
            System.out.println("forward:" + forward);
        }).until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) return false;

            return Math.abs(getDistance() - targetDistanceMeters) < 0.1
                    && Math.abs(target.getYaw()) < 1.0;
        }).finallyDo(interrupted -> drive.stop());
    }

    public Command driveToTagLEBRON(DriveSubsystem drive, double targetDistanceMeters) {
        return run(() -> {
            PhotonTrackedTarget target = getLockedTarget();

            if (target == null) {
                drive.stop();
                System.out.println("No target detected");
                return;
            }

            double yaw = target.getYaw();        // left/right angle
            double distance = getDistance();     // meters

            double yawError = yaw;
            double distanceError = distance - targetDistanceMeters;

            // Tunable gains
            double kTurn = 0.02;
            double kForward = 0.6;

            double turn = yawError * kTurn;
            double forward = distanceError * kForward;

            // Clamp speeds
            turn = Math.max(-0.4, Math.min(0.4, turn));
            forward = Math.max(-0.5, Math.min(0.5, forward));

            drive.driveArcade(forward, turn);

            System.out.println("Distance: " + distance);
            System.out.println("Yaw: " + yaw);
            System.out.println("Forward: " + forward);
            System.out.println("Turn: " + turn);
        })
        .until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) return false;

            double distanceError = Math.abs(getDistance() - targetDistanceMeters);
            double yawError = Math.abs(target.getYaw());

            return distanceError < 0.35 && yawError < 5.0;
        })
        .finallyDo(interrupted -> drive.stop());
    }

    // Turn robot until tag is centered
    public Command turnToTagTWOLEBRON(DriveSubsystem drive) {
        return run(() -> {
            PhotonTrackedTarget target = getLockedTarget();

            if (target == null) {
                drive.stop();
                return;
            }

            double yawError = target.getYaw();

            double kTurn = 0.02;
            double turn = yawError * kTurn;

            // Clamp turn speed
            turn = Math.max(-0.4, Math.min(0.4, turn));

            drive.driveArcade(0.0, turn);

            System.out.println("Yaw: " + yawError);
        })
        .until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            return target != null && Math.abs(target.getYaw()) < 1.5;
        })
        .finallyDo(interrupted -> drive.stop());
    }

    public Command driveToTagTWOLEBRON(DriveSubsystem drive, double targetDistanceMeters) {
        return run(() -> {
            PhotonTrackedTarget target = getLockedTarget();

            if (target == null) {
                drive.stop();
                System.out.println("No target");
                return;
            }

            double yaw = target.getYaw();
            double distance = getDistance();

            double yawError = yaw;
            double distanceError = distance - targetDistanceMeters;

            double kTurn = -0.02;
            double kForward = -0.6;

            double turn = yawError * kTurn;
            double forward = distanceError * kForward;

            // Clamp speeds
            turn = Math.max(-0.4, Math.min(0.4, turn));
            forward = Math.max(-0.5, Math.min(0.5, forward));

            drive.driveArcade(forward, turn);

            System.out.println("Distance: " + distance);
            System.out.println("Yaw: " + yaw);
        })
        .until(() -> {
            PhotonTrackedTarget target = getLockedTarget();
            if (target == null) return false;

            double distanceError = Math.abs(getDistance() - targetDistanceMeters);
            double yawError = Math.abs(target.getYaw());

            return distanceError < 0.15 && yawError < 2.0;
        })
        .finallyDo(interrupted -> drive.stop());
    }

    public Command autoSTOPCommand(DriveSubsystem drive) {
        return runOnce(() -> drive.stop());
    }
}
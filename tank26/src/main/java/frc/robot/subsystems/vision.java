package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

public class vision extends SubsystemBase {

    private final PhotonCamera camera;

    public vision() {
        camera = new PhotonCamera("photonvision"); // camera name in Photo Vision
    }

    // Returns horizontal yaw (X angle) in degrees
    public double getXAngle() {
        var result = camera.getLatestResult();
        if (result.hasTargets()) {
            PhotonTrackedTarget target = result.getBestTarget();
            return target.getYaw(); // degrees, +right / -left
        }
        return 0.0;
    }

    public boolean hasTarget() {

        return camera.getLatestResult().hasTargets();
    }
}
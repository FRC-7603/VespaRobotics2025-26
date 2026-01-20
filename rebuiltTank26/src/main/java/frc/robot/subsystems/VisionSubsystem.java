package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSubsystem extends SubsystemBase {

    private final PhotonCamera camera;

    public VisionSubsystem() {
        camera = new PhotonCamera("photonvision"); // camera name in Photon Vision
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
        if (target == null) return 0.0;
        return target.getYaw();
    }
}
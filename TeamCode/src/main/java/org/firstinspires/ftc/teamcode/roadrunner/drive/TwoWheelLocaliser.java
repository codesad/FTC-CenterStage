package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.roadrunner.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class TwoWheelLocaliser extends TwoTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.688975; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double PARALLEL_LATERAL = -7.28; // in; distance between the left and right wheels
    public static double PARALLEL_FORWARD = -2.62; // in; distance between the left and right wheels
    public static double PERPENDICULAR_LATERAL = 0; // in; offset of the lateral wheel
    public static double PERPENDICULAR_FORWARD = 4.17; // in; offset of the lateral wheel

    public static double X_MULTIPLIER = 1.009039668191;
    public static double Y_MULTIPLIER = 1.0;
    private final SampleMecanumDrive drive;

    private Encoder parallelEncoder, perpEncoder;

    private List<Integer> lastEncPositions, lastEncVels;

    public TwoWheelLocaliser(HardwareMap hardwareMap, SampleMecanumDrive drive) {
        super(Arrays.asList(
                new Pose2d(PARALLEL_FORWARD, PARALLEL_LATERAL, 0), // left
                new Pose2d(PERPENDICULAR_FORWARD, PERPENDICULAR_LATERAL, Math.toRadians(90)) // front
        ));

        this.drive = drive;

//        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intake")); // parallel
        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL")); // parallel
        perpEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "BL")); // perpendicular

        perpEncoder.setDirection(Encoder.Direction.REVERSE);

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        int rightPos = parallelEncoder.getCurrentPosition();
        int frontPos = perpEncoder.getCurrentPosition();

        return Arrays.asList(
                encoderTicksToInches(rightPos) * X_MULTIPLIER,
                encoderTicksToInches(frontPos) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {

        int rightVel = (int) parallelEncoder.getCorrectedVelocity();
        int frontVel = (int) perpEncoder.getCorrectedVelocity();


        return Arrays.asList(
                encoderTicksToInches(rightVel) * X_MULTIPLIER,
                encoderTicksToInches(frontVel) * Y_MULTIPLIER
        );
    }

    @Override
    public double getHeading() {
        return drive.getRawExternalHeading();
    }

    @Nullable
    @Override
    public Double getHeadingVelocity() {
        return drive.getExternalHeadingVelocity();
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;


public class Drivetrain {
    public DcMotor fl, fr, bl, br;

    public Drivetrain(HardwareMap hardwareMap)
    {
        fl = hardwareMap.get(DcMotor.class, "FL");
        fr = hardwareMap.get(DcMotor.class, "FR");
        bl = hardwareMap.get(DcMotor.class, "BL");
        br = hardwareMap.get(DcMotor.class, "BR");

        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        MotorConfigurationType motorConfigurationType = fr.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        fr.setMotorType(motorConfigurationType);

         motorConfigurationType = fl.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        fl.setMotorType(motorConfigurationType);

         motorConfigurationType = br.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        br.setMotorType(motorConfigurationType);

         motorConfigurationType = bl.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        bl.setMotorType(motorConfigurationType);
    }

    public void vectorMove(double x, double y, double t, double power)
    {
        double[] targetPower = normalize( new double[]{
                (x + y + t),
                (y - x - t),
                (y - x + t),
                (x + y - t)
        });

        fl.setPower(targetPower[0] * power);
        fr.setPower(targetPower[1] * power);
        bl.setPower(targetPower[2] * power);
        br.setPower(targetPower[3] * power);
    }

    public void vectorMoveFieldCentric(double x, double y, double t, double power, double headingRad) {
        y = -y;
        double xRot = (x * Math.cos(headingRad) - y * Math.sin(headingRad)) * SampleMecanumDrive.LATERAL_MULTIPLIER;
        double yRot = x * Math.sin(headingRad) + y * Math.cos(headingRad);

        double[] targetPower = normalize(new double[]{
                (xRot + yRot + t),
                (xRot - yRot - t),
                (xRot - yRot + t),
                (xRot + yRot - t)
        });


        fl.setPower(targetPower[0] * power * 1);
        fr.setPower(targetPower[1] * power * 0.9958980369);
        bl.setPower(targetPower[2] * power * 0.9795);
        br.setPower(targetPower[3] * power * 0.9608);
//
//        fl.setPower(targetPower[0] * power);
//        fr.setPower(targetPower[1] * power);
//        bl.setPower(targetPower[2] * power);
//        br.setPower(targetPower[3] * power);
    }

    private double[] normalize(double[] values)
    {
        // Put powers in the range of -1 to 1 only if they aren't already
        // Not checking would cause us to always drive at full speed
        if (Math.abs(values[0]) > 1 || Math.abs(values[2]) > 1 ||
                Math.abs(values[1]) > 1 || Math.abs(values[3]) > 1) {
            double max;
            max = Math.max(Math.abs(values[0]), Math.abs(values[2]));
            max = Math.max(Math.abs(values[1]), max);
            max = Math.max(Math.abs(values[3]), max);

            values[0] /= max;
            values[1] /= max;
            values[2] /= max;
            values[3] /= max;
        }
        return values;
    }
}